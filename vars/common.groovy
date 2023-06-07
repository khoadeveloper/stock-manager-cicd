def getAppPort(String service) {
    LinkedHashMap<String, String> portMap = [
            "eureka": "8000",
            "config": "8888"
    ]

    return portMap[service];
}