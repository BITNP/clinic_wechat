import wslite.rest.*
import wslite.http.HTTP
import grails.converters.JSON
import groovy.json.JsonSlurper

def API_HOST = "";
try{
    API_HOST =  request.getHeader('url');
    API_HOST = 'https://clinic.bitnp.net' + API_HOST
} catch(e){
    return ['errcode': 'header[url] get error: ' + e]
}
// e.g. https://clinic.bitnp.net/api/wechat/

// hack 一个 wslite 底层的错误，否则收到的响应会变成 ISO-8859-1 格式
// https://github.com/jwagenleitner/groovy-wslite/pull/101
HTTP.metaClass.'static'.getDEFAULT_CHARSET = { -> 'UTF-8' }
// 当然，也可以手动转码，但是这样不够优雅

// set apikey
def HEADERS = [apikey: "oh-my-tlb"];


// get badgeNumber
String badgeNumber = follower?.badgeNumber;
badgeNumber = badgeNumber? badgeNumber : "GUEST";

if (!badgeNumber) {
    return ['errcode': 'badgeNumber initailize error. ' + follower.toString() + '; request-header[url]:' + API_HOST]
}

// set query for backend authentication
def QUERY = [username: badgeNumber];

// badgeNumber = '1120143205'

def requestMethod = request.getMethod();
if (!requestMethod){
    return ['errcode':'mothod empty.' + e]
}

def client = new RESTClient(API_HOST);
client.httpClient.sslTrustAllCerts = true;

// def a =  client.httpClient.request.headers.'Content-Type'
// println a

def data = [:];
try {
    if (requestMethod == "POST"){

        def json_params = request.JSON;
        json_params.put("user", badgeNumber)

            data = client.post(
                    headers: HEADERS,
                    query: QUERY,
                    accept: ContentType.JSON,

                    ){
                    type ContentType.JSON
                    json request.JSON
            }
      //client.charset = 'GBK'
    } else if (requestMethod == "GET"){
        data = client.get(
                headers: HEADERS,
                query: QUERY,
                accept: ContentType.JSON,
                )
    } else if (requestMethod == "DELETE"){
        data = client.delete(
                headers: HEADERS,
                query: QUERY,
                accept: ContentType.JSON,
                )
    } else if (requestMethod == "PUT"){

        def json_params = request.JSON;
        // json_params.put("user", badgeNumber)
        // println json_params

        data = client.put(
                headers: HEADERS,
                query: QUERY,
                accept: ContentType.JSON,
                ){
                type ContentType.JSON
                json request.JSON
                }
    } else if (requestMethod == "OPTION"){
        data = client.option(
                headers: HEADERS,
                query: QUERY,
                accept: ContentType.JSON,
                body: params
                )
    } else {
        return requestMethod
    }
    if(!data?.contentAsString.isEmpty()) {
	    return  JSON.parse(data?.contentAsString)
    }
    else{ 
        return JSON.parse('{}')
    }
} catch (e) {

    return ['errcode': 'query resend error\n' + e.getResponse() ]
}
