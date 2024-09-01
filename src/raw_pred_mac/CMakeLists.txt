cmake_minimum_required(VERSION 3.28)
project(raw_pred_mac)

set(CMAKE_CXX_STANDARD 17)

# Add the executable
add_executable(raw_pred_mac main.cpp)

# Find and link CURL
find_package(CURL REQUIRED)
target_link_libraries(raw_pred_mac PRIVATE CURL::libcurl)

# Find the nlohmann_json package
find_package(nlohmann_json 3.2.0 REQUIRED)

# Include nlohmann_json headers
target_include_directories(raw_pred_mac PRIVATE ${nlohmann_json_INCLUDE_DIRS})

# Link the nlohmann_json library to your executable
target_link_libraries(raw_pred_mac PRIVATE nlohmann_json::nlohmann_json)