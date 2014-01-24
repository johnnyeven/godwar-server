function attack(skillId, attacker, defender, attackerCard, defenderCard)
{
	var def = attackerCard.getDef();
	var defChange = def * 1.05;

	var logList = new java.util.ArrayList();
	var log = new Packages.com.xgame.server.skill.AttackInfo();
	log.skillId = skillId;
	log.attacker = attacker;
	log.attackerCard = attackerCard;
	log.attackerDefChange = defChange - def;
	logList.add(log);
	return logList;
}
function attackArea(skillId, attacker, attackerCard, room)
{
	// var list = room.getList();
	// var defender;
	// var def;
	// var defChange;
	// var log;
	// var logList = new java.util.ArrayList();
	// for(var i = 0; i<list.size(); i++)
	// {
	// 	defender = list.get(i);
	// 	def = defender.getDef();
	// 	defChange = def * 1.05;

	// 	log = new Packages.com.xgame.server.skill.AttackLog();
	// 	log.attacker = attacker;
	// 	log.attackerDefChange = defChange - def;
	// 	logList.add(log);
	// }
	return null;
}