var mongoose = require('mongoose');
var app = express();

var len;
app.get('/', function(req, res){
  //res.send('id: ' + req.query.name0);
        console.log('connected');
        a=req.query.b;
        tel=req.query.c;

        console.log(a);
//      a=a.split(',');
        a=a.filter(function(item, pos){return a.indexOf(item)==pos;});
        //tel=tel.filter(function(item, pos){return tel.indexOf(item)==pos;});
        len=a.length;
        arr=Array(len);
        arr1=Array(len);
        for(var i=0;i<a.length;i++)
        {
                arr[i]=a[i];
                if(tel[i]=='emp')
                        {arr1[i]='facebook';}
                else
                        {arr1[i]=tel[i];}
        }

mongoose.connect('mongodb://localhost/local16');

var db = mongoose.connection;

db.on('error',console.error.bind(console, 'connection error'));
db.once('open',function callback(){console.log("open");});

var userSchema = mongoose.Schema({
name: 'string',
mobile: 'string'});
var user = mongoose.model('user',userSchema);
var user1;

for(var i=0;i<len;i++)
{
        user1 = new user({name: arr[i], mobile: arr1[i]});
        user1.save(function(err, user1){
        if(err && i%100==0)
        console.log("error");
        else if(i%100==0)
        console.log("saved");
});
}
//});

user.find({},function(err, users){
        var userMap = {};
        users.forEach(function(user){
        userMap[user.name] = user;
        });
        console.log(userMap);
        res.send(userMap);
        });

});


app.listen(8080);
