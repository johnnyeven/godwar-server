function attack(skillId, attacker, defender, attackerCard, defenderCard)
{
	var k = 1.13;
	var m = 100;
	var dex = 10;
	var d = 2.3 * dex / (10 + 2.3 * dex);
	var levelFix = 1.3;
	var attack = 35;
	var minAttack = attack * d;
	var maxAttack = attack;
	var defense = defenderCard.getMdef();
	var pdPercent = k * defense / (m + k * defense);
	var rand = new java.util.Random();
	var randNum = rand.nextFloat();
	var damage = 6;
	var damage = ((maxAttack - minAttack) * randNum + minAttack) * levelFix * (1 - pdPercent);

	var logList = new java.util.ArrayList();
	var log = new Packages.com.xgame.server.skill.AttackInfo();
	log.skillId = skillId;
	log.attacker = attacker;
	log.attackerCard = attackerCard;
	log.defender = defender;
	log.defenderCard = defenderCard;
	log.defenderHealthChange = -damage;

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