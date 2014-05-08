// var dialog = [
// 	{
// 		"content": "我是<风暴战将>加斯特，你现在所看到的我只是我的一个幻象，你要问我点什么？",
// 		"answer": [
// 			{
// 				"content": "这是一个怎样的世界？",
// 				"action": "goto",
// 				"position": 1
// 			},
// 			{
// 				"content": "没有什么要问的",
// 				"action": "close"
// 			}
// 		]
// 	},
// 	{
// 		"content": "这里是宇宙的边缘，被称作“尤格大陆”的世界，这里生活着六个种族无数的生灵，是一个神奇的国度……",
// 		"answer": [
// 			{
// 				"content": "原来是这样……",
// 				"action": "close"
// 			}
// 		]
// 	}
// ];

var dialog = new Packages.java.util.ArrayList();

var content1 = new Packages.com.xgame.server.scripts.NPCScriptContentParameter();
content1.content = "我是<风暴战将>加斯特，你现在所看到的我只是我的一个幻象，你要问我点什么？";

var answer1 = new Packages.com.xgame.server.scripts.NPCScriptAnswerParameter();
answer1.content = "这是一个怎样的世界？";
answer1.action = "goto";
answer1.position = 1;
content1.answer.add(answer1);

var answer2 = new Packages.com.xgame.server.scripts.NPCScriptAnswerParameter();
answer2.content = "没有什么要问的了……";
answer2.action = "close";
content1.answer.add(answer2);

dialog.add(content1);

var content2 = new Packages.com.xgame.server.scripts.NPCScriptContentParameter();
content2.content = "这里是宇宙的边缘，被称作“尤格大陆”的世界，这里生活着六个种族无数的生灵，是一个神奇的国度……";

var answer2_1 = new Packages.com.xgame.server.scripts.NPCScriptAnswerParameter();
answer2_1.content = "原来是这样……";
answer2_1.action = "close";
content2.answer.add(answer2_1);

dialog.add(content2);

function dialogue(step) {
	if(step > dialog.length) step = 0;
	return dialog.get(step);
}