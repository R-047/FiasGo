const request = require('request')
const OAuth = require('oauth-1.0a')
const crypto = require('crypto') // depenency package for OAuth-1.0a

// Token request function
function generateToken() {

    return new Promise((res, rej) => {
        // #1 Initialize OAuth with your HERE OAuth credentials from the credentials file that you downloaded above
        const oauth = OAuth({
            consumer: {
                key: 'vhDXTaJc4ZsaUv95llNWow', //Access key
                secret: '0MQM_CcUj154Db86SzK8hNYkZP8atKcKNvs1lBnCkmL5dJapq7iuBZgDTVZWY6YNSRDVano3G1nfZZlwjQosDA', //Secret key
            },
            signature_method: 'HMAC-SHA256',
            hash_function(base_string, key) {
                return crypto
                    .createHmac('sha256', key)
                    .update(base_string)
                    .digest('base64')
            },
        });


        // #2 Building the request object.
        const request_data = {
            url: 'https://account.api.here.com/oauth2/token',
            method: 'POST',
            data: { grant_type: 'client_credentials' },
        };


        request(
            {
                url: request_data.url,
                method: request_data.method,
                form: request_data.data,
                headers: oauth.toHeader(oauth.authorize(request_data)),
            },
            function (error, response, body) {

                if (response.statusCode == 200) {
                    let result = JSON.parse(response.body);
                    res(result.access_token)
                    //console.log('Token', result);
                }
            }
        );
    })

}



async function UseHereMaps(in_url){
    var token = await generateToken(); // passing the access_token 
    return new Promise((res, rej) => {
    
    console.log(token)
    var requestHeaders2 = { // Preparing the headers
        'Authorization': 'Bearer ' + token
    };


    var url = in_url;

    request(
        {
            url: url,
            method: 'GET',
            headers: requestHeaders2
        },
        function (error, response, body) {

            if (response.statusCode == 200) {
                let result = JSON.parse(response.body);
                res(result);
                //console.log('data', result);
            }
        }
    );
    })
}






// async function testMapsApi(){
// console.log("testing . . . ")
// const users_location_lat = 13.0508;
// const users_location_long = 77.5154;
// const result = await UseHereMaps(`https://discover.search.hereapi.com/v1/discover?at=${users_location_lat},${users_location_long}&limit=10&q=hospitals&in=countryCode:IND`)
// console.log(JSON.stringify(result, null, 4))
// // console.log(result.items[0].address.county +","+result.items[0].address.state);
// }

// testMapsApi();




module.exports = {UseHereMaps}
