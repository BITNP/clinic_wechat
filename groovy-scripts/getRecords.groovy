// 获得自己的工单历史
// 返回历史列表
import wslite.rest.*

String API_HOST = "http://clinic.bitnp.net/"
String API_PATH = "/wechat/"

String badgeNumber = follower?.badgeNumber
// badgeNumber = '1120143205'

if (!badgeNumber) {
    return ['errcode': 1]
}

def client = new RESTClient(API_HOST)
client.httpClient.sslTrustAllCerts = true

try {
    def data = client.post(
            headers: ['apikey': 'oh-my-tlb'],
            path: API_PATH,
            query: [username: badgeNumber]
    ).json

    return data
} catch (e) {
    return fail(e)
}
