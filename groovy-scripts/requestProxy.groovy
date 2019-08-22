import wslite.rest.*
// 作为代理
// 根据所选内容变化
String API_HOST = "http://clinic.bitnp.net/"
API_HOST = API_HOST + request.getRequestURI()
// e.g. http://clinic.bitnp.net/api/wechat/

// set apikey
def HEADERS = [apikey: "oh-my-tlb"]

// get badgeNumber
String badgeNumber = follower?.badgeNumber
if (!badgeNumber) {
    return ['errcode': 1]
}

// set query for backend authentication
def QUERY = [username: badgeNumber]

// badgeNumber = '1120143205'


def client = new RESTClient(API_HOST)
// client.httpClient.sslTrustAllCerts = true

try {

    def method = request.getMethod()

    if (request.method == "post"){
        def data = client.post(
            headers: HEADERS,
            query: QUERY,
            requestContentType: 'application/json',
            body: request.JSON
        ).json
    } else if (request.method == "get"){
        def data = client.get(
            headers: HEADERS,
            query: QUERY,
            requestContentType: 'application/json',
            body: request.JSON
    } else if (request.method == "delete"){
        def data = client.delete(
            headers: HEADERS,
            query: QUERY,
            requestContentType: 'application/json',
            body: request.JSON
    } else if (request.method == "put"){
        def data = client.put(
            headers: HEADERS,
            query: QUERY,
            requestContentType: 'application/json',
            body: request.JSON
    } else if (request.method == "option"){
        def data = client.option(
            headers: HEADERS,
            query: QUERY,
            requestContentType: 'application/json',
            body: request.JSON
    }

    return data
} catch (e) {

}
