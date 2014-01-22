function attack(attacker, defender)
{
	var def = defender.getDef();
	defender.setLastDef(def);	//important
	defender.setDef(def * 1.05);

	var log = new Packages.AttackLog();
	log.def = defender.getDef() - defender.getLastDef();
	return log;
}
function attackArea(attacker, room)
{
	var list = room.getList();
	var defender;
	var def;
	var log;
	var logList = new java.util.ArrayList();
	for(var i = 0; i<list.size(); i++)
	{
		defender = list.get(i);
		def = defender.getDef();
		defender.setLastDef(def);	//important
		defender.setDef(def * 1.05);

		log = new Packages.AttackLog();
		log.def = defender.getDef() - defender.getLastDef();
		logList.add(log);
	}
	return logList;
}