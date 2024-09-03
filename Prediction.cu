#include <iostream>
#include <fstream>
#include <string>
#include <list>
#include <vector>
#include <sstream>
#include <stdexcept>
#include <curl/curl.h>
#include <cuda_runtime.h>

/**
 * @brief CUDA kernel to predict the spot price of energy based on input features.
 *
 * @param d_demand Array of scheduled demand values.
 * @param d_generation Array of scheduled generation values.
 * @param d_semi_scheduled Array of semi-scheduled generation values.
 * @param d_net_import Array of net import values.
 * @param d_weather_data Array of weather data values.
 * @param d_spot_price Output array for predicted spot prices.
 * @param n Size of the input arrays.
 */
__global__ void predictSpotPriceKernel(double* d_demand, double* d_generation, double* d_semi_scheduled, double* d_net_import, double* d_weather_data, double* d_spot_price, int n) {
    int idx = blockIdx.x * blockDim.x + threadIdx.x;
    if (idx < n) {
        // Basic linear combination model for prediction (this should be replaced with a proper model)
        d_spot_price[idx] = 0.5 * d_demand[idx] + 0.2 * d_generation[idx] + 0.1 * d_semi_scheduled[idx] + 0.1 * d_net_import[idx] + 0.1 * d_weather_data[idx];
    }
}

/**
 * @brief Function to predict the spot prices using CUDA.
 *
 * @param demand Vector of scheduled demand values.
 * @param generation Vector of scheduled generation values.
 * @param semi_scheduled Vector of semi-scheduled generation values.
 * @param net_import Vector of net import values.
 * @param weather_data Vector of weather data values.
 * @return Vector of predicted spot prices.
 */
std::vector<double> predictSpotPrice(const std::vector<double>& demand, const std::vector<double>& generation, const std::vector<double>& semi_scheduled, const std::vector<double>& net_import, const std::vector<double>& weather_data) {
    int n = demand.size();
    std::vector<double> spot_price(n);

    double* d_demand, * d_generation, * d_semi_scheduled, * d_net_import, * d_weather_data, * d_spot_price;

    // Allocate device memory
    cudaMalloc(&d_demand, n * sizeof(double));
    cudaMalloc(&d_generation, n * sizeof(double));
    cudaMalloc(&d_semi_scheduled, n * sizeof(double));
    cudaMalloc(&d_net_import, n * sizeof(double));
    cudaMalloc(&d_weather_data, n * sizeof(double));
    cudaMalloc(&d_spot_price, n * sizeof(double));

    // Copy data from host to device
    cudaMemcpy(d_demand, demand.data(), n * sizeof(double), cudaMemcpyHostToDevice);
    cudaMemcpy(d_generation, generation.data(), n * sizeof(double), cudaMemcpyHostToDevice);
    cudaMemcpy(d_semi_scheduled, semi_scheduled.data(), n * sizeof(double), cudaMemcpyHostToDevice);
    cudaMemcpy(d_net_import, net_import.data(), n * sizeof(double), cudaMemcpyHostToDevice);
    cudaMemcpy(d_weather_data, weather_data.data(), n * sizeof(double), cudaMemcpyHostToDevice);

    // Launch the kernel with 256 threads per block
    int blockSize = 256;
    int numBlocks = (n + blockSize - 1) / blockSize;
    predictSpotPriceKernel << <numBlocks, blockSize >> > (d_demand, d_generation, d_semi_scheduled, d_net_import, d_weather_data, d_spot_price, n);

    // Copy the result back to host
    cudaMemcpy(spot_price.data(), d_spot_price, n * sizeof(double), cudaMemcpyDeviceToHost);

    // Free device memory
    cudaFree(d_demand);
    cudaFree(d_generation);
    cudaFree(d_semi_scheduled);
    cudaFree(d_net_import);
    cudaFree(d_weather_data);
    cudaFree(d_spot_price);

    return spot_price;
}

/**
 * @brief CUDA kernel to scale features using min-max normalization.
 *
 * @param d_data Input array to be scaled.
 * @param d_scaled_data Output array for scaled values.
 * @param min Minimum value in the dataset.
 * @param max Maximum value in the dataset.
 * @param n Size of the input array.
 */
__global__ void scaleFeaturesKernel(double* d_data, double* d_scaled_data, double min, double max, int n) {
    int idx = blockIdx.x * blockDim.x + threadIdx.x;
    if (idx < n) {
        d_scaled_data[idx] = (d_data[idx] - min) / (max - min);
    }
}

/**
 * @brief Function to scale features using min-max normalization.
 *
 * @param data Vector of input values to be scaled.
 * @return Vector of scaled values.
 */
std::vector<double> scaleFeatures(const std::vector<double>& data) {
    int n = data.size();
    std::vector<double> scaled_data(n);

    double* d_data, * d_scaled_data;

    // Find min and max in the data
    double min_val = *std::min_element(data.begin(), data.end());
    double max_val = *std::max_element(data.begin(), data.end());

    // Allocate device memory
    cudaMalloc(&d_data, n * sizeof(double));
    cudaMalloc(&d_scaled_data, n * sizeof(double));

    // Copy data from host to device
    cudaMemcpy(d_data, data.data(), n * sizeof(double), cudaMemcpyHostToDevice);

    // Launch the kernel
    int blockSize = 256;
    int numBlocks = (n + blockSize - 1) / blockSize;
    scaleFeaturesKernel << <numBlocks, blockSize >> > (d_data, d_scaled_data, min_val, max_val, n);

    // Copy the result back to host
    cudaMemcpy(scaled_data.data(), d_scaled_data, n * sizeof(double), cudaMemcpyDeviceToHost);

    // Free device memory
    cudaFree(d_data);
    cudaFree(d_scaled_data);

    return scaled_data;
}

/**
 * @brief CUDA kernel for predicting spot price using a polynomial regression model.
 *
 * @param d_demand Array of scaled demand values.
 * @param d_generation Array of scaled generation values.
 * @param d_semi_scheduled Array of scaled semi-scheduled generation values.
 * @param d_net_import Array of scaled net import values.
 * @param d_weather_data Array of scaled weather data values.
 * @param d_spot_price Output array for predicted spot prices.
 * @param n Size of the input arrays.
 */
__global__ void predictSpotPricePolyKernel(double* d_demand, double* d_generation, double* d_semi_scheduled, double* d_net_import, double* d_weather_data, double* d_spot_price, int n) {
    int idx = blockIdx.x * blockDim.x + threadIdx.x;
    if (idx < n) {
        // Polynomial regression model for prediction
        d_spot_price[idx] = 0.3 * d_demand[idx] * d_demand[idx]
            + 0.4 * d_generation[idx] * d_generation[idx]
            + 0.1 * d_semi_scheduled[idx] * d_semi_scheduled[idx]
            + 0.1 * d_net_import[idx]
            + 0.1 * d_weather_data[idx];
    }
}

/**
 * @brief Function to predict the spot prices using a polynomial regression model with CUDA.
 *
 * @param demand Vector of scaled demand values.
 * @param generation Vector of scaled generation values.
 * @param semi_scheduled Vector of scaled semi-scheduled generation values.
 * @param net_import Vector of scaled net import values.
 * @param weather_data Vector of scaled weather data values.
 * @return Vector of predicted spot prices.
 */
std::vector<double> predictSpotPricePoly(const std::vector<double>& demand, const std::vector<double>& generation, const std::vector<double>& semi_scheduled, const std::vector<double>& net_import, const std::vector<double>& weather_data) {
    int n = demand.size();
    std::vector<double> spot_price(n);

    double* d_demand, * d_generation, * d_semi_scheduled, * d_net_import, * d_weather_data, * d_spot_price;

    // Allocate device memory
    cudaMalloc(&d_demand, n * sizeof(double));
    cudaMalloc(&d_generation, n * sizeof(double));
    cudaMalloc(&d_semi_scheduled, n * sizeof(double));
    cudaMalloc(&d_net_import, n * sizeof(double));
    cudaMalloc(&d_weather_data, n * sizeof(double));
    cudaMalloc(&d_spot_price, n * sizeof(double));

    // Copy data from host to device
    cudaMemcpy(d_demand, demand.data(), n * sizeof(double), cudaMemcpyHostToDevice);
    cudaMemcpy(d_generation, generation.data(), n * sizeof(double), cudaMemcpyHostToDevice);
    cudaMemcpy(d_semi_scheduled, semi_scheduled.data(), n * sizeof(double), cudaMemcpyHostToDevice);
    cudaMemcpy(d_net_import, net_import.data(), n * sizeof(double), cudaMemcpyHostToDevice);
    cudaMemcpy(d_weather_data, weather_data.data(), n * sizeof(double), cudaMemcpyHostToDevice);

    // Launch the kernel
    int blockSize = 256;
    int numBlocks = (n + blockSize - 1) / blockSize;
    predictSpotPricePolyKernel << <numBlocks, blockSize >> > (d_demand, d_generation, d_semi_scheduled, d_net_import, d_weather_data, d_spot_price, n);

    // Copy the result back to host
    cudaMemcpy(spot_price.data(), d_spot_price, n * sizeof(double), cudaMemcpyDeviceToHost);

    // Free device memory
    cudaFree(d_demand);
    cudaFree(d_generation);
    cudaFree(d_semi_scheduled);
    cudaFree(d_net_import);
    cudaFree(d_weather_data);
    cudaFree(d_spot_price);

    return spot_price;
}

/**
 * @brief CUDA kernel to calculate Mean Absolute Error (MAE) between predicted and actual spot prices.
 *
 * @param d_actual Array of actual spot prices.
 * @param d_predicted Array of predicted spot prices.
 * @param d_errors Output array for absolute errors.
 * @param n Size of the input arrays.
 */
__global__ void calculateMAEKernel(double* d_actual, double* d_predicted, double* d_errors, int n) {
    int idx = blockIdx.x * blockDim.x + threadIdx.x;
    if (idx < n) {
        d_errors[idx] = abs(d_actual[idx] - d_predicted[idx]);
    }
}

/**
 * @brief Function to calculate Mean Absolute Error (MAE) between predicted and actual spot prices using CUDA.
 *
 * @param actual Vector of actual spot prices.
 * @param predicted Vector of predicted spot prices.
 * @return The Mean Absolute Error (MAE).
 */
double calculateMAE(const std::vector<double>& actual, const std::vector<double>& predicted) {
    int n = actual.size();
    std::vector<double> errors(n);

    double* d_actual, * d_predicted, * d_errors;

    // Allocate device memory
    cudaMalloc(&d_actual, n * sizeof(double));
    cudaMalloc(&d_predicted, n * sizeof(double));
    cudaMalloc(&d_errors, n * sizeof(double));

    // Copy data from host to device
    cudaMemcpy(d_actual, actual.data(), n * sizeof(double), cudaMemcpyHostToDevice);
    cudaMemcpy(d_predicted, predicted.data(), n * sizeof(double), cudaMemcpyHostToDevice);

    // Launch the kernel
    int blockSize = 256;
    int numBlocks = (n + blockSize - 1) / blockSize;
    calculateMAEKernel << <numBlocks, blockSize >> > (d_actual, d_predicted, d_errors, n);

    // Copy the result back to host
    cudaMemcpy(errors.data(), d_errors, n * sizeof(double), cudaMemcpyDeviceToHost);

    // Free device memory
    cudaFree(d_actual);
    cudaFree(d_predicted);
    cudaFree(d_errors);

    // Calculate the mean of errors
    double mae = std::accumulate(errors.begin(), errors.end(), 0.0) / n;

    return mae;
}

std::vector<std::string> splitString(const std::string& str, char delimiter) {
    std::vector<std::string> result;
    std::stringstream ss(str);
    std::string item;

    while (std::getline(ss, item, delimiter)) {
        result.push_back(item);
    }

    return result;
}

class AEMO_data
{
public:
    AEMO_data(std::string date, double Spot_Price, double Scheduled_Demand, double Scheduled_Generation, double Semi_Scheduled_Generation, double net_import, std::string type);
    ~AEMO_data();

    static AEMO_data format_data(const std::string& raw_data);
};

AEMO_data::AEMO_data(std::string date, double Spot_Price, double Scheduled_Demand, double Scheduled_Generation, double Semi_Scheduled_Generation, double net_import, std::string type)
{
}

AEMO_data::~AEMO_data()
{
}

AEMO_data AEMO_data::format_data(const std::string& raw_data) {
    auto split = splitString(raw_data, ',');

    if (split.size() != 7)
        throw std::invalid_argument("size of string must be 7");

    if (split[0].empty() || split[1].empty() || split[2].empty() || split[3].empty() || split[4].empty() || split[5].empty() || split[6].empty())
        throw std::invalid_argument("Split data had null elements");

    return AEMO_data(split[0], std::stod(split[1]), std::stod(split[2]), std::stod(split[3]), std::stod(split[4]), std::stod(split[5]), split[6]);
}

// This function will be called by libcurl to write received data
size_t WriteCallback(void* contents, size_t size, size_t nmemb, std::string* s) {
    size_t totalSize = size * nmemb;
    s->append((char*)contents, totalSize);
    return totalSize;
}

void getWeatherData() {
    CURL* curl;
    CURLcode res;
    std::string readBuffer;

    curl = curl_easy_init();  // Initialize a CURL session
    if (curl) {
        // Set the URL for the request
        curl_easy_setopt(curl, CURLOPT_URL, "http://www.example.com");

        // Set the write function callback to store the response data
        curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, WriteCallback);

        // Set the pointer to the response data
        curl_easy_setopt(curl, CURLOPT_WRITEDATA, &readBuffer);

        // Perform the request, res will get the return code
        res = curl_easy_perform(curl);

        // Check for errors
        if (res != CURLE_OK)
            std::cerr << "curl_easy_perform() failed: " << curl_easy_strerror(res) << std::endl;
        else
            std::cout << "Response data: " << readBuffer << std::endl;

        // Always cleanup
        curl_easy_cleanup(curl);
    }
}

int main() {
    std::string temp;
    std::ifstream data_file("current_data.csv");
    std::list<AEMO_data> data;

    CURL* curl; 

    while (std::getline(data_file, temp)) {
        if (temp[0] == 'S')
            continue;

        data.push_back(AEMO_data::format_data(temp));
    }

    data_file.close();
}
