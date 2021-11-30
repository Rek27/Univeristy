// CTS1: Savo, Simeunovic (Can not find my ID)

public abstract class InGerman {
	// I tried to put comments but I had to delete them because file was too big :)
	// I think the code is readable without comments
	public static String inGerman(int number) {
		String translation = "";
		if(number > 999999 || number < 0) {
			translation = "<<<unbekannt>>>";
		} else if(number == 0) {
			translation = "null";
		}else if(number>99999){
			translation = sixDigits(number);
		}else if(number>9999){
			translation = fiveDigits(number);
		}else if(number>999){
			translation = fourDigits(number);
		}else if(number>99){
			translation = threeDigits(number);
		}else if(number>9){
			translation = translateTwoDigitNumber(number);
		}else if(number==1){
			translation = "eins";
		}else if(number>0){
			translation = translateDigit(number);
		}
		return translation;
	}
	
	private static String sixDigits(int number) {
		return translateDigit(number/100000) + "hundert" + fiveDigits(number%100000);
	}
	
	private static String fiveDigits(int number) {
		if(number>9999) {
			return translateTwoDigitNumber(number/1000) + "tausend" + threeDigits(number%1000);
		}
		return fourDigits(number);
	}
	
	private static String fourDigits(int number) {
		if(number>999) {
			return translateDigit(number/1000) + "tausend" + threeDigits(number%1000);
		}
		return "tausend" + threeDigits(number);
	}
	
	private static String threeDigits(int number) {
		if(number%100 == 1) {
			if(number==1) {
				return "eins";
			}
			return translateDigit(number/100) + "hunderteins";
		}
		if(number>99) {
			return translateDigit(number/100) + "hundert" + translateTwoDigitNumber(number%100);
		}
		return translateTwoDigitNumber(number%100);
	}
	

	private static String translateTwoDigitNumber(int number) {
		if(number == 10) {
			return "zehn";
		}else if(number == 11) {
			return "elf";
		}else if(number == 0) {
			return "";
		}else if(number == 12) {
			return "zwölf";
		}else if(number == 17) {
			return "siebzehn";
		}else if(number == 16) {
			return "sechzehn";
		}else if(number<10) {
			return translateDigit(number);
		}else if(number%10==0){
			return translateFirstDigitOfTwoDigitNumber(number/10);
		}else if(number<20){
			return translateDigit(number%10) + "zehn";
		}else {
			return translateDigit(number%10) + "und" + translateFirstDigitOfTwoDigitNumber(number/10);
		}
		
	}

	private static String translateFirstDigitOfTwoDigitNumber(int digit) {
		if(digit==2) {
			return "zwanzig";
		}else if(digit==7){
			return "siebzig";
		}else if(digit==3){
			return "dreißig";
		}else if(digit==6){
			return "sechzig";
		}else{
			return translateDigit(digit) + "zig";
		}
	}

	private static String translateDigit(int digit) {
		if(digit==1) {
			return "ein";
		}else if(digit==2) {
			return "zwei";
		}else if(digit==3){
			return "drei";
		}else if(digit==4){
			return "vier";
		}else if(digit==5){
			return "fünf";
		}else if(digit==6){
			return "sechs";
		}else if(digit==7){
			return "sieben";
		}else if(digit==8){
			return "acht";
		}else if(digit==9) {
			return "neun";
		}
		return "";
	}

}
