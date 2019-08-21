import wslite.rest.*

// 根据所选内容变化
String API_HOST = "http://clinic.bitnp.net/api/wechat/2/"

String badgeNumber = follower?.badgeNumber
// badgeNumber = '1120143205'
String appointmentTime = params?.appointment_time

if (!badgeNumber) {
    return ['errcode': 1]
}

def client = new RESTClient(API_HOST)
client.httpClient.sslTrustAllCerts = true

try {
    def data = client.post(
            headers: ['apikey': 'oh-my-tlb'],
            query: [username: badgeNumber],
            requestContentType: 'application/json',
            body: [appointment_time:appointmentTime,
            campus:campus,description:description,phone_num:phone_num,
            realname:realname]
    ).json

    return data
} catch (e) {

}
