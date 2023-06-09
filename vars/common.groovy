def getAppPort(String service) {
    LinkedHashMap<String, String> portMap = [
            "eureka": "8000",
            "config": "8888",
            "gateway": "8001",
            "admin": "8010",
            "kiotviet": "8011"
    ]

    return portMap[service];
}