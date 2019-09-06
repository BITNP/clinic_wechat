package script_playground

import wslite.rest.*
import grails.converters.JSON
import javax.servlet.http.HttpServletRequest


class ApiController {

    def index() {

    // !mock data, will be deleted in production
    def follower = [badgeNumber:"1120171224"]
    
    def proxy =  { HttpServletRequest request->
    // 作为代理
    // 根据所选内容变化
    def API_HOST = request.getHeader('url')

    // !MOCK WILL BE DELETED
    API_HOST = API_HOST? API_HOST: "https://clinic.bitnp.net/api/wechat/"
    // println "[index][" + request.getMethod() + "] visit:" + API_HOST 
    // e.g. https://clinic.bitnp.net/api/wechat/

    // set apikey
    def HEADERS = [apikey: "oh-my-tlb"]


    // get badgeNumber
    String badgeNumber = follower?.badgeNumber
    if (!badgeNumber) {
        return ['errcode': 'badgeNumber initailize error. ' + follower.toString() + '; request-header[url]:' + API_HOST]
    }

    // set query for backend authentication
    def QUERY = [username: badgeNumber]

    // badgeNumber = '1120143205'

    def requestMethod = request.getMethod()
    if (!requestMethod)
        return ['errcode':'mothod empty.' + e]


    def client = new RESTClient(API_HOST)
    client.httpClient.sslTrustAllCerts = true
    
   
    try {
        def data = [:]
        if (requestMethod == "POST"){

            def json_params = request.JSON
            json_params.put("user", badgeNumber)

            data = client.post(
                headers: HEADERS,
                query: QUERY,
                accept: ContentType.JSON,
                
            ){
                type ContentType.JSON
                json request.JSON
            }
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

            def json_params = request.JSON
            // json_params.put("user", badgeNumber)
            println json_params

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
        
        return data
        } catch (e) {
                // println "\n\n\n"
                // println e.message
                println e.getResponse().getContentAsString()
                // println "\n\n\n"

                return ['errcode': 'query resend error\n' + e ]
            }
        }



        def res = proxy(request);
        // println '[index]return value:' + res.contentAsString;
        // if(!res.contentAsString){
        //     println '[null] ' + res
        // }
        render contentType: "text/json", text: res.contentAsString
    }
}
