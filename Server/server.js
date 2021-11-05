const express = require("express");
const cors = require('cors')
const cookieParser = require("cookie-parser");
const expressSession = require("express-session");
const { Authenticate } = require("./middlewares/authenticate");
const {AdminAuthenticate} = require("./middlewares/adminAuth")
const bodyParser = require("body-parser");
const app = express();
const path = require("path")

const corsOptions = {
	origin: [
		"http://localhost:3000",
		"http://127.0.0.1:3000",
		"http://104.142.122.231",
		"http://127.0.0.1:5500"
	      ],
	credentials: true
      };
app.use(cors(corsOptions));


app.get("/", (req, res) => {
	console.log("hi");
	console.log(req.headers)
	res.send("hello")

})

// app.use(express.static(path.join(__dirname, "admins")));




//utility middlewares
app.use(cookieParser());
app.use(expressSession({ secret: "its a secret"}));
app.use(bodyParser.urlencoded({
	extended: true
}))




//admins
app.use("/adminLogin", require("./routes/AdminsRouter").adminLogin)
app.use("/getCampInfo", AdminAuthenticate, require("./routes/AdminsRouter").getCampInfoRoute)
app.use("/updateUserStatus", AdminAuthenticate, require("./routes/AdminsRouter").updateUserStatusRoute)
app.use("/updateSupportStatus", AdminAuthenticate, require("./routes/AdminsRouter").updateSupportStatusRoute)
app.use("/getSupports", AdminAuthenticate, require("./routes/AdminsRouter").getSupportsRouter)
app.use("/getReports", AdminAuthenticate, require("./routes/AdminsRouter").getReports)
app.use("/removePost", AdminAuthenticate, require("./routes/AdminsRouter").takeDownPosts)
app.use("/getPost", AdminAuthenticate, require("./routes/AdminsRouter").getSinglePost)
app.use("/getUsers", AdminAuthenticate, require("./routes/AdminsRouter").getUsersRouter)
//common
app.use('/admingetCampsImages', AdminAuthenticate, require("./routes/getCampImagesRoute").getCampsImagesRouter)
app.use('/admingetPostImage', AdminAuthenticate, require("./routes/getPostImagesRoute").getPostImagesRouter);
app.use('/admingetUserDp', AdminAuthenticate, require("./routes/getSetUserDpRoute").getDpImageRouter);
app.use('/admingetServices', AdminAuthenticate, require("./routes/getSetServicesPostRoute").getServicesPostsRouter);
app.use('/admingetVictims', AdminAuthenticate, require("./routes/getSetvictimsPostRoute").getVictimsPostsRouter);




//users
app.use('/register', require("./routes/registerRoute"));
app.use("/login", require("./routes/loginRoute"));

app.use(Authenticate);

app.use('/testRoute', require("./routes/testRoute"));

app.use('/setUserDp', require("./routes/getSetUserDpRoute").uploadDpImageRouter);
app.use('/getUserDp', require("./routes/getSetUserDpRoute").getDpImageRouter);
app.use('/postServices', require("./routes/getSetServicesPostRoute").setServicesPostRouter);
app.use('/postVictims', require("./routes/getSetvictimsPostRoute").setVictimsPostRouter);
app.use('/getServices', require("./routes/getSetServicesPostRoute").getServicesPostsRouter);
app.use('/getVictims', require("./routes/getSetvictimsPostRoute").getVictimsPostsRouter);
app.use('/getPostImage', require("./routes/getPostImagesRoute").getPostImagesRouter);
app.use('/getCamps', require("./routes/getSetCampsRoute").getCamps);
app.use('/setCamps', require("./routes/getSetCampsRoute").setCamps);
app.use('/getCampsImages', require("./routes/getCampImagesRoute").getCampsImagesRouter);
app.use('/victimsUpvotes', require("./routes/upvotesRouter").victimsUpvotesRouter);
app.use('/serviceUpvotes', require("./routes/upvotesRouter").servicesUpvotesRouter);
app.use('/postComment', require("./routes/commentsRouter").postCommentsRouter);
app.use('/getComments', require("./routes/commentsRouter").getCommentsRouter);
app.use('/deleteComment', require("./routes/commentsRouter").deleteCommentsRouter);
app.use('/getActivities', require("./routes/getUserActivitesRoute").getUserActivitiesRouter);
app.use('/removeCamp', require("./routes/removeCampRouter").removeCampsRouter);
app.use('/getUsersCamps', require("./routes/getSetCampsRoute").getUsersCamps);
app.use('/supportVictim', require("./routes/supportVictimRouter").supportVictimRouter)
app.use('/getVictimsItems', require("./routes/getVictimsItemsRoute").getVictimsItemRouter);
app.use('/getPostSupports', require("./routes/supportVictimRouter").getPostSupportRouter);
app.use("/reportPost", require("./routes/reportPostRouter").reportPostRouter)








//login
//registration
//co-ordinates of maps
//victims posts
//service posts
//upload victims post
//uplooad service post
//get news
//Users Activity
const port = process.env.PORT || 3000;
app.listen(port, () => console.log("server running on port 3000"))