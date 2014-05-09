var dialog = [
	{
		"title": "&lt;<font color='0xFFFF00'>风暴战将</font>&gt;加斯特的幻像",
		"content": "我是<风暴战将>加斯特，你现在所看到的我只是我的一个幻象，你要问我点什么？",
		"answer": [
			{
				"content": "这是一个怎样的世界？",
				"action": "goto",
				"position": 1
			},
			{
				"content": "没有什么要问的",
				"action": "close"
			}
		]
	},
	{
		"title": "&lt;<font color='0xFFFF00'>风暴战将</font>&gt;加斯特的幻像",
		"content": "这里是宇宙的边缘，被称作“尤格大陆”的世界，这里生活着六个种族无数的生灵，是一个神奇的国度……",
		"answer": [
			{
				"content": "原来是这样……",
				"action": "close"
			}
		]
	}
];

function dialogue(step) {
	if(step > dialog.length) step = 0;
	return dialog[step];
}