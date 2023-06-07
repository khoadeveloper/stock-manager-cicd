def portMap = [
        "eureka": "8000",
        "config": "8888"
]

def getAppPort(String service) {
    return portMap[service];
}