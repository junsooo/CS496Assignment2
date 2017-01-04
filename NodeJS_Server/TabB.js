var val = 0;
var str;
function myFunction(){
        str='picture'+val;
        val++;
}

var picSchema = mongoose.Schema({
        id: 'string',
        image: 'string'});


var User = mongoose.model('User', picSchema);
User.find().remove().exec();


app.get('/', function(req, res){


        User.find({}, function(err,user){
                if(err) throw err;
                var userMap={};
                user.forEach(function(doc){
                        userMap[doc.id]=doc.image;
                });
                res.send(userMap);


        });


        console.log('Connected')


});
app.post('/', function(req, res){
        var json = req.body;

        myFunction();
        user1 = new User({id: str, image: json.type});
        user1.save(function(err, user1){
                if(err){
                        throw err;
                }
                console.log('saved');

        });

        res.send('upload successful');
        console.log('get POST request');
});
app.delete('/', function(req,res){
        var json=req.body;
        User.remove({id: json.type}, function(err, user1){
                if(err){
                        console.log('delete fail');
                        res.send('deleted image');
                }
                else{
                        res.send('delete success');
                        User.remove(user1);
                }
        });

        console.log('got DELETE request');
});




app.listen(80, function(){ console.log('Connect 80 port');});
