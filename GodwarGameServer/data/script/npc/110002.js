var dialog = [
	{
		"title": "&lt;<font color='0xFFFF00'>打不赢</font>&gt;唐虾",
		"content": "我叫唐虾，是一个乱入的NPC，你看到我不奇怪，因为我是被文兄大人创造出来的，一切皆有可能……",
		"answer": [
			{
				"content": "这个……那个……你出现在这里有什么意图？",
				"action": "goto",
				"position": 1
			},
			{
				"content": "(好奇怪的一个人，还是离他远远的好……)",
				"action": "close"
			}
		]
	},
	{
		"title": "&lt;<font color='0xFFFF00'>打不赢</font>&gt;唐虾",
		"content": "可笑，你觉得我出现在这里是没有必要的吗？我能做的事情太多了！比如：",
		"answer": [
			{
				"content": "打不赢",
				"action": "goto",
				"position": 2
			},
			{
				"content": "上面那个是开玩笑的，我能做算术",
				"action": "goto",
				"position": 3
			},
			{
				"content": "打开卡牌窗口",
				"action": "command",
				"command": "ShowCardMediatorCommand.ShowNote"
			},
			{
				"content": "完全不想和你说话……",
				"action": "close"
			}
		]
	},
	{
		"title": "&lt;<font color='0xFFFF00'>打不赢</font>&gt;唐虾",
		"content": "我就是打不赢，你tia我撒",
		"answer": [
			{
				"content": "(这人完全无法沟通……)",
				"action": "close"
			}
		]
	}
];

function dialogue(step) {
	if(step == 3) {
		return {
			"title": "错误",
			"content": "很明显是骗你的，算术功能还没开发完",
			"answer": [
				{
					"content": "……",
					"action": "close"
				}
			]
		};
	} else if(step > dialog.length) {
		step = 0;
	}
	return dialog[step];
}