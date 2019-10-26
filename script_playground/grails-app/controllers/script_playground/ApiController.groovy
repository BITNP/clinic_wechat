package script_playground

import wslite.rest.*
import wslite.http.HTTP
import grails.converters.JSON
import groovy.json.JsonSlurper
import javax.servlet.http.HttpServletRequest
import java.security.MessageDigest

class ApiController {

    def index() {

        // !mock data, will be deleted in production
        def follower = [badgeNumber:"1120171224"];


        def proxy =  { HttpServletRequest request->
            // 作为代理
            // 根据所选内容变化
String HOST = 'http://localhost:8000'
String HOST_MASK = 'http://example.org'
def URL = "";
try{
    URL =  request.getHeader('url');
    if(URL.startsWith(HOST_MASK)){
        // absolute url
        URL = URL.replace(HOST_MASK, HOST)
    } else if (!URL.startsWith('http')) {
        // relative url
        URL = HOST + URL
    } else {
        // malicious url
        return ['errcode':"reject."]
    }
} catch(e){
    return ['errcode': 'header[url] get error: ' + e]
}
// e.g. https://clinic.bitnp.net/api/wechat/

// hack 一个 wslite 底层的错误，否则收到的响应会变成 ISO-8859-1 格式
// https://github.com/jwagenleitner/groovy-wslite/pull/101
HTTP.metaClass.'static'.getDEFAULT_CHARSET = { -> 'UTF-8' }
// 当然，也可以手动转码，但是这样不够优雅

// define md5 function
def generateMD5 = { String text ->
    // define md5 function
    MessageDigest md5 = MessageDigest.getInstance("MD5");
    md5.update(text.getBytes());
    return new BigInteger(1, md5.digest()).toString(16);
}

// get badgeNumber
String badgeNumber = follower?.badgeNumber;
badgeNumber = badgeNumber? badgeNumber : "GUEST";


// println request.getRemoteAddr()

// set apikey
String apikey = "oh-my-tlb";

String time = new Date().toString();

apikey = generateMD5(apikey + badgeNumber + time)

def HEADERS = ['X-API-KEY': apikey, Date: time, 'X-Forwarded-For':request.getRemoteAddr()];


if (!badgeNumber) {
    return ['errcode': 'badgeNumber initailize error. ' + follower.toString() + '; request-header[url]:' + URL]
}

// set query for backend authentication
def QUERY = [username: badgeNumber];

// badgeNumber = '1120143205'

def requestMethod = request.getMethod();
if (!requestMethod){
    return ['errcode':'mothod empty.' + e]
}

def client = new RESTClient(URL);
client.httpClient.sslTrustAllCerts = true;
client.defaultCharset = 'UTF-8'

println '[sendto]' + URL
// def a =  client.httpClient.request.headers.'Content-Type'
// println a

def data = [:];
def content = "";
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
        // never get in here.
        return requestMethod
    }
    if(!data?.contentAsString.isEmpty()) {
        content = data?.contentAsString
    }
    else{ 
        content = '{}'
    }
} catch (e) {
    // println "\n\n\n"
    // println e.message
    //println e.getResponse().getContentAsString();
    // println "\n\n\n" 
    // println 'error'
    // println 'data:' + data.getResponse().contentAsString
    // println 'error:' + e.message
    if(e && e.response){
        response.status = e.response.statusCode
        content = e.response.contentAsString
    }
}
content = content.replace(HOST,HOST_MASK)
// println content
return JSON.parse(content)
}



        def res = proxy(request);
        render  res as JSON
    }
}
