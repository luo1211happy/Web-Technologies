<?php
//phpinfo();
	$flag = 0;
	if (isset($_GET["street"]) && !empty($_GET["street"])) {//Checks if street value exists
		$flag = $flag + 1;
	}
	if (isset($_GET["city"]) && !empty($_GET["city"])) {//Checks if city value exists
		$flag = $flag + 1;
	}
	if (isset($_GET["state"]) && !empty($_GET["state"])) {//Checks if state value exists
		$flag = $flag + 1;
	}
	if (isset($_GET["degree"]) && !empty($_GET["degree"])) {//Checks if degree value exists
		$flag = $flag + 1;
	}
	if ($flag == 4) {
		parse_jason();
	}
//Function to check if the request is an AJAX request
function is_ajax() {
	return isset($_SERVER['HTTP_X_REQUESTED_WITH']) && strtolower($_SERVER['HTTP_X_REQUESTED_WITH']) == 'xmlhttprequest';
}

function parse_jason() {
	$street = $_GET["street"];
	$city = $_GET["city"];
	$state = $_GET["state"];
	$degree = $_GET["degree"];
	$street = str_replace(" ", "+", urlencode($street));
	//have to encode url elements or it can not be parsed
	//echo "$address<br>";
	$city = str_replace(" ", "+", urlencode($city));
	//echo "$city<br>";
	$googleurl = "https://maps.googleapis.com/maps/api/geocode/xml?address=" . $street . ",+" . $city . ",+" . $state . "&key=AIzaSyCA70Tnu0XO50J3lQbcBeyecbU7WEVdSVs";
	//echo $googleurl."<br>";
	// echo $googleurl."<br>";
	$xmlobj = simplexml_load_file($googleurl);
	if ($xmlobj -> count() == 1)//use count to balance whether the xmlobj is null or not
	{
		echo "geo_error";
	} else {
		foreach ($xmlobj->children() as $child) {
			//echo $child."<br>";
			foreach ($child->children() as $child2) {
				if ($child2 -> getName() == "geometry") {
					foreach ($child2->children() as $child3) {
						if ($child3 -> getName() == "location") {
							$lat = $child3 -> lat;
							$lng = $child3 -> lng;
							// echo $lat ."<br>";
							// echo $lng."<br>";
						}
					}
				}
			}
		}
		if ($degree == "fahrenheit") {
			$unit = "us";
		} else {
			$unit = "si";
		}
		$jasonurl = "https://api.forecast.io/forecast/75b77f672a867485e0877d91eb21dc73/" . $lat . "," . $lng . "?units=" . $unit . "&exclude=flags";
		//$jasonurl = "";
		//echo "$jasonurl";
		$contents = file_get_contents($jasonurl);
		$contents = utf8_encode($contents);
		//$contents = jason_encode($contents);
		//if it is not jason which can not be transmitted
		echo $contents;
	}

}
?>