
//request = "command://delete/0";
if(/^(command:\/\/)/.test(request) ){
	var cmds = request.split("//")[1].split("/");
	if(cmds!=null){
		//print(cmds[0]);
		//print(cmds[1]);
		switch(cmds[0]){
			case 'delete':
				var id = cmds[1];
				deleteitem(id);
			break;
			case 'edit':
				var id = cmds[1];
				edititem(id);
			break;
		}
	}
}