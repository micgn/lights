var jsonStr = rest.get("http://kamk01.campus-m.fiducia.de:8080/job/KKV_TRUNK/api/json");
var json = JSON.parse(jsonStr);
var result = parseInt(json.healthReport[0].score);
if (result < 99) logWindow.say(result);
if (result < 100) command.setRed(true);