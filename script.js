function getScore(url) {
  var jsonStr = rest.get(url);
  var json = JSON.parse(jsonStr);
  return parseInt(json.healthReport[0].score);
}


if (getScore("http://myserver:8080/job/TRUNK/api/json") < 100) {

  command.yellow.set(true, 100, 5000);

  command.green.set(true);

  command.red.set(true, 3000, 500);
}

logWindow.say("executed");

