<?php
	$servers = array();
	if(!empty($_POST[]))
		if()
	
	echo $servers;

	function addServer($ip) {
		$file = $file('serverlist.txt', 'r+');
		fwrite($file,$ip);
		
		fclose($file);
	}
	
	function removeServer($ip) {
		$file = fopen('serverlist.txt', 'r+');
		
		$lines = array();
		
		$count = 0;
		while(feof($file)) {
			$lines[] = fgets($file);
			if(FALSE !== strpos($line, $ip, 0))
				unset($lines[$count]);
				
			$count += 1;
		}
		
		fclose($file);
		unlink('serverlist.txt');
		$file = fopen('serverlist.txt', 'w+');
		for($lines as $l)
			fwrite($file, $l);
		fclose($file);
	}
	
	function getServers() {
		return implode('\r\n',file("serverlist.txt"));
	}
?>