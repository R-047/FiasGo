const {registerUser, authuser, updateUserStatus, getUserActivites} = require("./UserModel")
const {uploadServicesPost, uploadVictimsPost, getVictimsFeed, getServicesFeeds, upvoteVictimsPost, upvoteServicesPost, downvoteVictimsPost, downvoteServicePost, PostComment, getPostComment, deletePostComment,  takeDownVictimsPost,  takeDownServicesPost, getVictimsPost, getServicePost, getUsersVictimsPost, getUsersServicesPost} = require("./PostModel")
const {getCampsModel, postCampsModel, takeDownCamp, getCampInfo, getUsersCamps} = require("./MapsModel")
const {supportVictim,getVictimsItems,updateSupportStatus,getSupports,getPostSupports,getUserSupports} = require("./SupportsModel")
const {reportVictimsPost,reportServicePost,getReportsVictimsPost,getReportsServicesPost} = require("./ReportsModel")
const {getTime} = require('../Utils/getPostgresTime');


async function test(){
	
	// const result = await registerUser("alby", "alby123@gmail.com", "2222", "9090909090", getTime(),null)
	// const result = await authuser("rohit@gmail.com", "2234")
	// const result = await getUserActivites(3);
	// const result = await updateUserStatus(38, 'banned') //--admin

	//pass
	// const result = await uploadServicesPost(3, "0909090909", "test post only", ["abc.png", "bosd.png"], 12.3375  ,75.8069, "coorg", getTime());
	// const result = await getServicesFeeds(12.4244, 75.7382);
// 	const result = await uploadVictimsPost(1, "test post only", ["abc.png", "bosd.png"],"tumkur",
// 	[{
// 		item_name: "jockey",
// 		item_desc: "u know it",
// 		limit: 5
// 	},
// 	{
// 		item_name: "choco",
// 		item_desc: "five star",
// 		limit: 10
// 	},
// 	{
// 		item_name: "cig",
// 		item_desc: "malbro",
// 		limit: 10
// 	}
// ], getTime());

	//const result = await getVictimsFeed();


	//pass
	// const result = await postCampsModel("ulala", "1234567890","2021-11-1", "2020-11-10", "10:30", "15:30",3, "just for fun",13.0055 ,77.5692, "malleshwaram", "9090909090",["abc.png", "bosd.png", "bcd.png"])
	//const result = await getCampsModel(13.0508, 77.5154);
	// takeDownCamp, getCampInfo, getUsersCamps
	//const result = await takeDownCamp(21); 
	//const result = await getCampInfo(22); --admin
	//const result = await getUsersCamps(1);

	//pass
	//const result = await upvoteServicesPost(1, 62);
	//const result = await downvoteVictimsPost(1, 55);
	//const result = await downvoteServicePost(1, 62);
	//const result = await PostComment(1, 62, "helloooooooooooooo", "2021-10-27", "21:16");
	//const result = await getPostComment(62);
	//const result = await deletePostComment(2, 62);



	
	//pass
	// const result = await supportVictim(3,"another help", 67,33,3,getTime());
	//const result = await getVictimsItems(57);
	//const result = await updateSupportStatus("delivered", 7); --admin
	//const result = await getSupports(); --admin
	//const result = await getPostSupports(55);
	//const result = await getUserSupports(3);
	
	//pass
	//const result = await reportVictimsPost(55, 1, "bad post");
	//const result = await reportServicePost(62, 1, "bad service")
	//const result = await getReportsServicesPost(62); --admin
	//const result = await getReportsVictimsPost(55);--admin

	//pass
	//const result = await takeDownServicesPost(63); --admin
	//const result = await takeDownVictimsPost(54); --admin
	//const result = await getServicePost(62); --admin
	//const result = await getVictimsPost(55); --admin
	//const result = await getUsersVictimsPost(1);
	//const result = await getUsersServicesPost(1);
	

	

	
	console.log(result.rows)
	//console.log(result2)
}




//console.log(getTime());
test();
