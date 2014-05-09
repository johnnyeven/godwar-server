function convertDialogueContent(obj) {
	var content = new Packages.com.xgame.server.scripts.NPCScriptContentParameter();
	content.title = obj.title;
	content.content = obj.content;

	for(var i = 0; i<obj.answer.length; i++) {
		var answer = new Packages.com.xgame.server.scripts.NPCScriptAnswerParameter();
		answer.content = obj.answer[i].content;
		answer.action = obj.answer[i].action;
		if(obj.answer[i].position) {
			answer.position = obj.answer[i].position;
		}
		content.answer.add(answer);
	}
	
	return content;
}