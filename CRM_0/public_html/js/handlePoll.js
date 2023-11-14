handlePoll=function (event)
{
event.cancel();
var poll = AdfPage.PAGE.findComponent('pollid');
if (0 > poll.getInterval())
poll.setInterval(1000);
else
poll.setInterval(-1);
}