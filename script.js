function getScore(url) {
  var jsonStr = rest.get(url);
  var json = JSON.parse(jsonStr);
  return parseInt(json.healthReport[0].score);
}


if (getScore("http://kamk01.campus-m.fiducia.de:8080/job/KKV_TRUNK/api/json") < 100) {

  command.yellow.set(true, 100, 5000);

  command.green.set(true);

  command.red.set(true, 3000, 500);
}

logWindow.say("executed");

