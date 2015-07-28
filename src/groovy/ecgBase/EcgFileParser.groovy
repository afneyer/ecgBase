package ecgBase

/*
 * The result of the parsing is to fill the EcgDAO object which is not dependent on the file type
 */
abstract class EcgFileParser {
	
	def EcgDataFile ecgDataFile
	
	EcgFileParser( EcgDataFile file ) {
		ecgDataFile = file;
	}
	
	abstract EcgDAO parse()

}