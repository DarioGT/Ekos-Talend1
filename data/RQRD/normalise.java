// no modifications
output_row.PMID = input_row.PMID;
output_row.Year = input_row.Year;
output_row.LastName = input_row.LastName;
output_row.ForeName = input_row.ForeName;
output_row.Initials = input_row.Initials;

// output_row.Year_EColl_ = input_row.Year_EColl_;
// output_row.IdType = input_row.IdType;

// concatenate FULLNAME
if (StringHandling.LEN(input_row.LastName)>0 && StringHandling.LEN(input_row.ForeName)>0) {
	output_row.FullName = TalendString.removeAccents(input_row.LastName) + ", " + TalendString.removeAccents(input_row.ForeName);
}


// cleanup Non Standard AFFILIATION ===========================================

String rawAff =  "";

if (input_row.Affiliations.indexOf("From") > -1 ) {
	String strToken = "";
	String tmpStr = StringHandling.RIGHT(input_row.Affiliations, StringHandling.LEN(input_row.Affiliations) - 4);
	int lenStr = StringHandling.LEN(tmpStr);
	int posSemiColon = 0;
	
	// get complete INITIALS
	String strInitials = "";
	strInitials = strInitials + StringHandling.LEFT(input_row.ForeName, 1) + ".";
	if ( StringHandling.LEN(input_row.Initials) == 2 ) {
		strInitials = strInitials + StringHandling.RIGHT(input_row.Initials, 1) + ".";
	}
	strInitials = strInitials + StringHandling.LEFT(input_row.LastName, 1) + ".";
	
	while (StringHandling.LEN(tmpStr) > 0) {
		lenStr = StringHandling.LEN(tmpStr);
		posSemiColon = tmpStr.indexOf(";");
		if (posSemiColon < 0) {
			strToken = tmpStr;
			tmpStr = "";
		} else {
			strToken = StringHandling.LEFT(tmpStr, posSemiColon);
			tmpStr = StringHandling.RIGHT(tmpStr, lenStr-(posSemiColon+1));
		}// if (posSemiColon < 0)
		
		// check if INITIALS are in the STRTOKEN
		if (strToken.indexOf(strInitials) > -1 ) {
			rawAff = StringHandling.TRIM( StringHandling.LEFT( strToken, strToken.indexOf("(") ) );
			tmpStr = ""; // get only first affiliation
		}
		
		
	}// while (lenStr > 0)
	 
} else {
	rawAff = input_row.Affiliations;
} // if (rawAff.indexOf("From") == 1 )



// add USA if last string token have only 2 chars and seem a US state
if (StringHandling.LEN(rawAff) - rawAff.lastIndexOf(",") <= 4 ) {
	rawAff = rawAff + ", USA";
}


output_row.rawAffiliation = rawAff;



// process YEAR ===========================================


// int tmpYear = 10000;
// if (input_row.Year != null) {
// 	tmpYear = input_row.Year;
// }
// if (input_row.Year_EColl_ != null) {
// 	if(input_row.Year_EColl_ < tmpYear) {
// 		tmpYear = input_row.Year_EColl_ ;
// 	}
// } 
// output_row.Year = tmpYear;


// process UNIVERSITY ===========================================

String tmpStr = rawAff;
String strToken = "";
String university = "";
int lenStr = StringHandling.LEN(tmpStr);
int firstComa = 0;
int semiColon = 0;

while (lenStr > 0) {
	firstComa = tmpStr.indexOf(",");
	semiColon = tmpStr.indexOf(";");
	if (firstComa < 0 ) {
		strToken = tmpStr;
		tmpStr = "";	
	} else {
		strToken = StringHandling.LEFT(tmpStr, firstComa);
		tmpStr = StringHandling.RIGHT(tmpStr, lenStr-(firstComa+1));
	}		  	
	if (strToken.indexOf("niversity") > 0 || 
		strToken.indexOf("niversité") > 0 || 
		strToken.indexOf("niversite") > 0 ||
		strToken.indexOf("niversidad") > 0||
		strToken.indexOf("Univ.") > 0) {
		university = StringHandling.TRIM(strToken);
		tmpStr = "";
	}
	lenStr = StringHandling.LEN(tmpStr);
}
if (university.indexOf("]") > 0) {
	university = StringHandling.RIGHT(university, StringHandling.LEN(university)-(university.indexOf("]")+1));
}
if (university.indexOf("and") == 1 ) {
	university = StringHandling.RIGHT(university, StringHandling.LEN(university)-(university.indexOf("and") + 3));
}
if (university.indexOf(" ") > 0  && university.indexOf(" ") < 3) {
	university = StringHandling.RIGHT(university, StringHandling.LEN(university)-2);
} 
output_row.University = StringHandling.TRIM(university);


// process DEPARTMENT ===========================================

tmpStr = rawAff;
strToken = "";
String department = "";
lenStr = StringHandling.LEN(tmpStr);
firstComa = 0;

while (lenStr > 0) {
	firstComa = tmpStr.indexOf(",");
	semiColon = tmpStr.indexOf(";");
	if (firstComa < 0 ) {
		strToken = tmpStr;
		tmpStr = "";	
	} else {
		strToken = StringHandling.LEFT(tmpStr, firstComa);
		tmpStr = StringHandling.RIGHT(tmpStr, lenStr-(firstComa+1));
	}		  	
	if (strToken.indexOf("epartment") > 0 || 
		strToken.indexOf("épartement") > 0 || 
		strToken.indexOf("epartament") > 0 || 
		strToken.indexOf("Dpt.") > 0 || 
		strToken.indexOf("Division") > 0 ||
		strToken.indexOf("Unit") > 0 ) {
		department = StringHandling.TRIM(strToken);
		tmpStr = "";
	}
	lenStr = StringHandling.LEN(tmpStr);
}
if (department.indexOf(";") > 0) {
	department = StringHandling.RIGHT(department, StringHandling.LEN(department)-(department.indexOf(";")+1));
}
if (department.indexOf("and") == 1 ) {
	department = StringHandling.RIGHT(department, StringHandling.LEN(department)-(department.indexOf("and") + 3));
}
if (department.indexOf(" ") > 0  && department.indexOf(" ") < 3) {
	department = StringHandling.RIGHT(department, StringHandling.LEN(department)-2);
} 
output_row.Department = StringHandling.TRIM(department);



// process RESEARCH CENTER / INSTITUTE / LAB===========================================

tmpStr = rawAff;
strToken = "";
String resCenter = "";
lenStr = StringHandling.LEN(tmpStr);
firstComa = 0;

while (lenStr > 0) {
	firstComa = tmpStr.indexOf(",");
	semiColon = tmpStr.indexOf(";");
	if (firstComa < 0 ) {
		strToken = tmpStr;
		tmpStr = "";	
	} else {
		strToken = StringHandling.LEFT(tmpStr, firstComa);
		tmpStr = StringHandling.RIGHT(tmpStr, lenStr-(firstComa+1));
	}		  	
	if (strToken.indexOf("nstitute") > 0 || 
		strToken.indexOf("nstitut") > 0 || 
		strToken.indexOf("Instit.") > 0 ||
		strToken.indexOf("Center") > 0 || 
		strToken.indexOf("center") > 0 || 
		strToken.indexOf("Ctr.") > 0 || 
		strToken.indexOf("Lab") > 0 ) {
		resCenter = StringHandling.TRIM(strToken);
		tmpStr = "";
	}
	lenStr = StringHandling.LEN(tmpStr);
}
if (resCenter.indexOf(";") > 0) {
	resCenter = StringHandling.RIGHT(resCenter, StringHandling.LEN(resCenter)-(resCenter.indexOf(";")+1));
}
if (resCenter.indexOf("and") == 1 ) {
	resCenter = StringHandling.RIGHT(resCenter, StringHandling.LEN(resCenter)-(resCenter.indexOf("and") + 3));
}
if (resCenter.indexOf(" ") > 0  && resCenter.indexOf(" ") < 3) {
	resCenter = StringHandling.RIGHT(resCenter, StringHandling.LEN(resCenter)-2);
} 
output_row.ResearchCenter = StringHandling.TRIM(resCenter);


// process HOSPITAL/CLINIC ===========================================

tmpStr = rawAff;
String hospital = "";
lenStr = StringHandling.LEN(tmpStr);
firstComa = 0;

while (lenStr > 0) {
	firstComa = tmpStr.indexOf(",");
	semiColon = tmpStr.indexOf(";");
	if (firstComa < 0 ) {
		strToken = tmpStr;
		tmpStr = "";	
	} else {
		strToken = StringHandling.LEFT(tmpStr, firstComa);
		tmpStr = StringHandling.RIGHT(tmpStr, lenStr-(firstComa+1));
	}		  	
	if (strToken.indexOf("ospital") > 0 || 
		strToken.indexOf("opital") > 0 || 
		strToken.indexOf("ôpital") > 0 ||
		strToken.indexOf("linic") > 0) {
		hospital = StringHandling.TRIM(strToken);
		tmpStr = "";
	}
	lenStr = StringHandling.LEN(tmpStr);
}
output_row.Hospital = StringHandling.TRIM(hospital);




// process AFFILIATION =====================================

String organization = "";
String address = "";
String province = "";
String country = "";
tmpStr = rawAff;

// CLEANUP AFFILIATION =============
if (tmpStr.indexOf("@") != -1) {
	tmpStr = StringHandling.TRIM(StringHandling.LEFT(tmpStr, tmpStr.lastIndexOf(" ")));
}	
if (tmpStr.indexOf(";") != -1) {
	tmpStr = StringHandling.TRIM(StringHandling.LEFT(tmpStr, tmpStr.indexOf(";")));
}
//if ( tmpStr.matches("/[A-Za-z]\d[A-Za-z] ?\d[A-Za-z]\d/") ) {
//		remove the postal code
//}


int lastDot = tmpStr.lastIndexOf(".");
lenStr = StringHandling.LEN(tmpStr);

// get COUNTRY
int lastComa = tmpStr.lastIndexOf(",");		  	
if (lastComa > 0 && lenStr > 0) {
	country = StringHandling.RIGHT(tmpStr, lenStr - (lastComa + 1));
	lastDot = country.indexOf(".");
	if (lastDot > 0) {
		country = StringHandling.LEFT(country, lastDot);
	}
	country = StringHandling.TRIM(country);
	output_row.Country = country;
	tmpStr = StringHandling.LEFT(tmpStr, lastComa);

	// get PROVINCE
	lenStr = StringHandling.LEN(tmpStr);
	lastComa = tmpStr.lastIndexOf(",");
	if (lastComa > 0  && lenStr > 0 ) {
		province = StringHandling.RIGHT(tmpStr, lenStr - (lastComa+1));
		province = StringHandling.TRIM(province);
		tmpStr = StringHandling.LEFT(tmpStr, lastComa);
		if (!StringHandling.IS_ALPHA(province)){
			lenStr = StringHandling.LEN(tmpStr);
			lastComa = tmpStr.lastIndexOf(",");
			province = StringHandling.RIGHT(tmpStr, lenStr - (lastComa+1));
			province = StringHandling.TRIM(province);
		}
		output_row.Province = StringHandling.TRIM(province);
		
		
		// get ORGANIZATION
		//lenStr = StringHandling.LEN(tmpStr);
		//firstComa = tmpStr.indexOf(",");
		//if (firstComa > 0  && lenStr > 0) {
		//	organization = StringHandling.LEFT(tmpStr, firstComa);
		//	output_row.Organization = StringHandling.TRIM(organization);
		//	tmpStr = StringHandling.RIGHT(tmpStr, lenStr - (firstComa+1));
			  		

		//	// get ADDRESS
		//	lenStr = StringHandling.LEN(tmpStr);
		//	if (lenStr > 0  && lenStr > 0) {
		//		output_row.Address = StringHandling.TRIM(tmpStr);	  				
		//	}// if ADDRESS	
			
		//}// if ORGANISATION	
		
	}// if PROVINCE
	
}// if COUNTRY



// put as organization either UNIVERSITY, either HOSPITAL/CLINIC, either RESEARCH CENTER
if (StringHandling.LEN(university) > 0) {
	output_row.Organization = StringHandling.TRIM(university);
} else if (StringHandling.LEN(hospital) > 0) {
	output_row.Organization = StringHandling.TRIM(hospital);
} else if (StringHandling.LEN(resCenter) > 0) {
	output_row.Organization = StringHandling.TRIM(resCenter);
} else if (StringHandling.LEN(department) > 0) {
	output_row.Organization = StringHandling.TRIM(department);
} else {
	output_row.Organization = "";
}

