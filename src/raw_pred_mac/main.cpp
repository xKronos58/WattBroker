#include <iostream>
#include <fstream>
#include <string>
#include <list>
#include <utility>
#include <vector>
#include <sstream>
#include <stdexcept>
#include <curl/curl.h>
#include <nlohmann/json.hpp>
#include <unistd.h>

/**
 * @breif Main C++ class for prediction ** BEFORE CUDA IMPLEMENTATION **
 *
 * This file stores the main code used to predict the total
 * generation, usage and price of energy based of several factors
 * such as weather and time of day. This class works in tandem with
 * a CUDA based algorithm to comprehend data and *learn from
 * previous pricing and *modify values based on an efficiency calculation
 * performed every so-often (decided by user)
 * */

std::vector<std::string> splitString(const std::string& str, char delimiter) {
    std::vector<std::string> result;
    std::stringstream ss(str);
    std::string item;

    while (std::getline(ss, item, delimiter)) {
        result.push_back(item);
    }

    return result;
}

enum weatherType {
    Solar, Wind, Hydro
};

/**
 * @brief Class to store AEMO data
 *
 * This class stores the data attained from the AEMO API
 * and stores it in a class to be used for further calculations
 * and predictions.
 * */
class AEMO_data
{
public:
    AEMO_data(const std::string& date, double Spot_Price, double Scheduled_Demand, double Scheduled_Generation,
              double Semi_Scheduled_Generation, double net_import, const std::string& type);
    ~AEMO_data() = default;

    static AEMO_data format_data(const std::string& raw_data);
};

AEMO_data::AEMO_data(const std::string& date, double Spot_Price, double Scheduled_Demand, double Scheduled_Generation,
                     double Semi_Scheduled_Generation, double net_import, const std::string& type)
{
    std::cout << date << std::endl << Spot_Price << std::endl << Scheduled_Demand << std::endl << Scheduled_Generation
    << std::endl << Semi_Scheduled_Generation << std::endl << net_import << std::endl << type;
}



/**
 * @breif formats AEMO source data
 *
 * Takes in a CSV of ARMO attained data to be formatted and stored
 * in the AEMO class to have further modification run on it.
 * */
AEMO_data AEMO_data::format_data(const std::string& raw_data) {
    auto split = splitString(raw_data, ',');

    if (split.size() != 7)
        throw std::invalid_argument("size of string must be 7");

    if (split[0].empty() || split[1].empty() || split[2].empty() || split[3].empty() || split[4].empty() ||
    split[5].empty() || split[6].empty())
        throw std::invalid_argument("Split data had null elements");

    return {split[0], std::stod(split[1]), std::stod(split[2]),
            std::stod(split[3]), std::stod(split[4]),
            std::stod(split[5]), split[6]};
}

/**
 * @breif called by libcurl to write and receive data.
 * */
size_t WriteCallback(void* contents, size_t size, size_t nmemb, std::string* s) {
    size_t totalSize = size * nmemb;
    s->append((char*)contents, totalSize);
    return totalSize;
}

/**
 * @breif gets data from a url
 *
 * Takes in a url to curl data from apis
 * based on an http-request * in this case
 * used to attain weather data but will
 * also be modified for AEMO data *
 * */
std::string getWeatherData(const std::string& _url) {
    CURL* curl;
    CURLcode res;
    std::string readBuffer;

    curl = curl_easy_init();  // Initialize a CURL session
    if (curl) {
        // Set the URL for the request
        curl_easy_setopt(curl, CURLOPT_URL, _url.c_str());

        // Set the write function callback to store the response data
        curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, WriteCallback);

        // Set the pointer to the response data
        curl_easy_setopt(curl, CURLOPT_WRITEDATA, &readBuffer);

        // Perform the request, res will get the return code
        res = curl_easy_perform(curl);

        // Check for errors
        if (res != CURLE_OK) {
            std::cerr << "curl_easy_perform() failed: " << curl_easy_strerror(res) << std::endl;
            throw std::exception();
        }

        // Always cleanup
        curl_easy_cleanup(curl);
        return readBuffer;
    }

    return "NaN";
}

/**
 * @breif gets weather data from the provided source
 *
 * Takes in the weather type (Referencing to a get-like to BOM
 * and retrieves the data then returns a string with the json
 * response to be converted into useful data and used to calculate
 * generation from renewable sources.
 * */
std::string getData(double lat, double lng, weatherType type) {
    switch (type) {
        case Solar :
            return "https://api.open-meteo.com/v1/forecast?latitude=" + std::to_string(lat) + "&longitude=" + std::to_string(lng) +
                   "&hourly=temperature_2m,cloud_cover,cloud_cover_low,cloud_cover_mid,cloud_cover_high,surface_temperature,is_day,sunshine_duration&daily=temperature_2m_max,temperature_2m_min,sunrise,sunset,daylight_duration,sunshine_duration,uv_index_max,uv_index_clear_sky_max&models=bom_access_global";
        case Wind :
            return "https://api.open-meteo.com/v1/forecast?latitude=" + std::to_string(lat) + "&longitude=" + std::to_string(lng) +
                   "&hourly=temperature_2m,precipitation,snowfall,surface_pressure,wind_speed_120m,wind_gusts_10m&daily=wind_speed_10m_max,wind_gusts_10m_max,wind_direction_10m_dominant&wind_speed_unit=ms&models=bom_access_global";
        case Hydro :
            return "https://google.com"; /*TODO : create prediction dataset for hydro
 *          Plan :
 *              Previous rainfall
 *              Temp (<0.1º)
 *              Snowfall (if tmp >0.0º)
 *              wind-speed (0m)
 *              Snowpack and snow-melt ( amount of melted snow )
 *              */
    }

    return "NaN";
}

/**
 * @brief Class to store data about solar farms
 *
 * This class stores weather and power output information
 * about solar farms for each farm read from the AEMO database.
 * This class will call CUDA methods to take the provided
 * data and predict efficiency and generation which can then be
 * later cross applied with the USAGE prediction methods
 * for accurate solar generation predictions and pricing.
 * @author Finley Crowther
 * */
class solarWeather {
private:
    double latitude;
    double longitude;
    double elevation;
    std::string timezone;
    nlohmann::json hourly;
    nlohmann::json timeArray;
    nlohmann::json temperatureArray;
public:
    explicit solarWeather(const std::string& data);
    ~solarWeather() = default;
    int getTemperatureNow();
};

/**
 * @breif reads weather data from curl request from BOM
 *
 * Takes in a JSON string formatted for solar farms reads the
 * data and stores the values. Used to determine how much each
 * energy each solar panel will produce at any given time to
 * allow to algorithm to more accurately predict the total
 * generation of the area being calculated.
 * */
solarWeather::solarWeather(const std::string& data) {
    if(data.empty())
        return; // No implementation data
    nlohmann::json jsonObj = nlohmann::json::parse(data);
    latitude = jsonObj["latitude"];
    longitude = jsonObj["longitude"];
    timezone = jsonObj["timezone"];
    elevation = jsonObj["elevation"];
    hourly = jsonObj["hourly"];
    timeArray = hourly["time"];
    temperatureArray = hourly["temperature_2m"];

//    for (size_t i = 0; i < timeArray.size(); ++i) {
//        std::string time = timeArray[i];
//        double temperature = temperatureArray[i];
//        std::cout << "Time: " << time << ", Temperature: " << temperature << "°C" << std::endl;
//    }

}

int solarWeather::getTemperatureNow() {
    if (!temperatureArray.empty()) {
        // Assuming the temperature is stored as a double and needs to be returned as an integer
        return static_cast<int>(temperatureArray[0].get<double>());
    }
    return 0; // Or some other default value if the array is empty
}

/**
 * @brief Enum to store classification type
 *
 * Stores the type of classification to determine which
 * energy gird it will be on and how it will be scheduled
 * to generate energy */
enum classification {
    SemiScheduled, Scheduled
};

/**
 * @brief Stores solar farm information and related data from AEMO data
 *
 * This class stores all relevant solar farm information used to
 * predict the generation and grid impact of a each individual solar farm.
 * The class contains basic methods to convert data and parse data but
 * is mostly data storage.
 * */
class solarFarm {
private:
    std::string participant; double latitude; double longitude; std::string stationName; std::string region;
    std::string dispatchType; std::string category; classification _classification; std::string fuel_source_primary;
    std::string fuel_source_Descriptor; std::string technology_type_primary; std::string technology_type_descriptor;
    std::string physical_unit_number; double unit_size; bool aggregation; std::string DUID; double reg_cap;
    double max_cap; double max_roc; solarWeather weather = solarWeather("");
public:
    solarFarm(std::string participant, double latitude, double longitude, std::string stationName, std::string region,
              std::string dispatchType, std::string category, classification _classification, std::string fuel_source_primary,
              std::string fuel_source_Descriptor, std::string technology_type_primary, std::string technology_type_descriptor,
              std::string physical_unit_number, double unit_size, bool aggregation, std::string DUID, double reg_cap,
              double max_cap, double max_roc, const solarWeather& weather);
    std::string toString();
};

solarFarm::solarFarm(std::string participant, double latitude, double longitude, std::string stationName,
                     std::string region, std::string dispatchType, std::string category, classification _classification,
                     std::string fuel_source_primary, std::string fuel_source_Descriptor, std::string technology_type_primary,
                     std::string technology_type_descriptor, std::string physical_unit_number, double unit_size,
                     bool aggregation, std::string DUID, double reg_cap, double max_cap, double max_roc, const solarWeather& weather) {
    this->participant = std::move(participant);
    this->latitude = latitude;
    this->longitude = longitude;
    this->stationName = std::move(stationName);
    this->region = std::move(region);
    this->dispatchType = std::move(dispatchType);
    this->category = std::move(category);
    this->_classification = _classification;
    this->fuel_source_primary = std::move(fuel_source_primary);
    this->fuel_source_Descriptor = std::move(fuel_source_Descriptor);
    this->technology_type_primary = std::move(technology_type_primary);
    this->technology_type_descriptor = std::move(technology_type_descriptor);
    this->physical_unit_number = std::move(physical_unit_number);
    this->unit_size = unit_size;
    this->aggregation = aggregation;
    this->DUID = std::move(DUID);
    this->reg_cap = reg_cap;
    this->max_cap = max_cap;
    this->max_roc = max_roc;
    this->weather = weather;
}

std::string solarFarm::toString() {
    return "Station : " + this->stationName + ", Longitude : " +
    std::to_string(this->longitude) + ", Latitude : " +
    std::to_string(this->latitude) + ", Temperature : " +
    std::to_string(this->weather.getTemperatureNow()) + "°C";
}

/**
 * @breif Converts data to a <strong>solarFarm</strong>
 *
 * Takes each line from the _PV_SOLAR_FARMS.csv and converts
 * them to the <strong>solarFarm</strong> class
 * */
solarFarm convertSolarFarm(const std::string& str) {
    auto split = splitString(str, ',');

    if (split.size() != 19)
        throw std::invalid_argument("size of string must be 19");

    if (split[0].empty() || split[1].empty() || split[2].empty() || split[3].empty() || split[4].empty() ||
        split[5].empty() || split[6].empty() || split[7].empty() || split[8].empty() || split[9].empty() ||
        split[10].empty() || split[11].empty() || split[12].empty() || split[13].empty() || split[14].empty() ||
        split[15].empty() || split[16].empty() || split[17].empty() || split[18].empty())
        throw std::invalid_argument("Split data had null elements");

    solarWeather weather = solarWeather(getWeatherData(getData(std::stod(split[1]), std::stod(split[2]), weatherType::Solar)));

    return {split[0], std::stod(split[1]), std::stod(split[2]), split[3], split[4], split[5], split[6],
            split[7] == "Semi-Scheduled" ? classification::SemiScheduled : classification::Scheduled, split[8],
            split[9], split[10], split[11], split[12], std::stod(split[13]), split[14] == "Y", split[15],
            std::stod(split[16]), std::stod(split[17]), std::stod(split[18]), weather};
}

/**
 * @breif Reads a csv list of solar farms
 *
 * Takes a list of solar farms pulled from the AEMO API
 * then converts them to a <strong>solarFarm</strong> for later use.
 *
 * TODO format for async HTTP-get requests to speed up the process
 * */
std::list<solarFarm> readSolarFarms() {
    std::ifstream solarFarmLocation("/Users/finleycrowther/CLionProjects/raw_pred_mac/_PV_SOLAR_FARMS.csv");
    std::string temp;
    std::list<solarFarm> data;

    while(std::getline(solarFarmLocation, temp)) {
        if (temp[0] == 'P')
            continue;

        data.push_back(convertSolarFarm(temp));
    }

    return data;
}

int main() {
    std::string temp;
    std::ifstream data_file("current_data.csv");
    std::list<AEMO_data> data;

    std::list<solarFarm> solarFarms = readSolarFarms();

    for(solarFarm s : solarFarms) {
        std::cout << s.toString() << std::endl;
    }

    while (std::getline(data_file, temp)) {
        if (temp[0] == 'S')
            continue;

        data.push_back(AEMO_data::format_data(temp));
    }

    data_file.close();
}
