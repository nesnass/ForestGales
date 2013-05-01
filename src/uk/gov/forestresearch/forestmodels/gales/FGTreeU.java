package uk.gov.forestresearch.forestmodels.gales;

public class FGTreeU
{
    private static double SNOWDENSITY = 0.15*1000; //{Jackson 1977}
    private static int MAX_NO_OF_TREE_SECTIONS = 50; //{maximum number we divide tree into}
    private static double TAPERPOWER = 0.5;// {power used to calculate stem shape as function of distance from top of tree}


    //private static int MAX_NO_OF_TREE_SECTIONS = 10;
    private double [] MAX_NO_OF_TREE_SECTIONS_Array =  new double [ MAX_NO_OF_TREE_SECTIONS ] ;


  //public  { Public declarations }
    private int treeCharacteristicsCounter; //Stores the number of time that treecharcteristics is run
    private String species ;
    private double topHeight;
    private double meanDBH;
    private String forceMethod ;
    private int yc;
    private boolean thinning;
    private double initSpacing;
    private double snowDepth;
    private int noOfSections;   // number of sections for the calculation of tree diameters
    private String [] dominances;
    private String dominance;
    private double meanHeight;
    protected double canopyBdth ;       // Used to calculate tree Charecteristics
    protected double canopyDepth;       // Used to calculate tree Charecteristics
    protected double stemDensity;       // Used to calculate tree Charecteristics
    public void setStemDensity(double stemDensity) {
		this.stemDensity = stemDensity;
	}

	protected double canopyDensity;     // Used to calculate tree Charecteristics
    public void setCanopyDensity(double canopyDensity) {
		this.canopyDensity = canopyDensity;
	}

	protected double canopyHeight;      // Used to calculate tree Charecteristics
    protected double midCanopy;         // Used to calculate tree Charecteristics
    public double snowWeight;           // Used to calculate tree Charecteristics
    public double stemWeight;          // Used to calculate tree Charecteristics
    protected double branchVolume;     // Used to calculate tree Charecteristics
    protected double branchWeight;     // Used to calculate tree Charecteristics
    public double [] Z  = new double [MAX_NO_OF_TREE_SECTIONS];        // height on tree sections
    public double []Diam  = new double [MAX_NO_OF_TREE_SECTIONS];         // diameters up tree}
    public double []mass  = new double [MAX_NO_OF_TREE_SECTIONS];         // masses up tree = stem, branch and snow mass}
    protected double []MSnow  = new double [MAX_NO_OF_TREE_SECTIONS];        // snow mass up tree}
    protected double []branchWidth  = new double [MAX_NO_OF_TREE_SECTIONS];  // branch width up tree}

    public double DBH_Dominance(String dominance,  double meanDbh)
    {
        //Dominance = (doNone, doMean, doDominant, doSubDominant);
          // convert centimeters to metres
          meanDBH = meanDBH * 100; // surely this should be a division?
          double A = 0.6829 * meanDBH - 2.0795; //-2.0795 added by RAD 991012 as this is included in spreadsheet diamdistributions.xls
          double B = 0.0;
          if(  meanDBH > 50 ) B = 0.0084;
          else B = -1.5E-07 * Math.pow (meanDBH, 3) + 1.85E-05 * Math.pow (meanDBH, 2) -0.00073536 * meanDBH + 0.01791;                                      // old values
          
          if (dominance.equalsIgnoreCase("dominant" ) ) return (A * Math.exp(B * 75.0)) / 100.0;                             //1
          else if( dominance.equalsIgnoreCase("subDominant") )return A * Math.exp((B * 25.0)) / 100.0;                   //2
          else if( dominance.equalsIgnoreCase("mean") )return meanDBH; //option if actual tree data is used  //9
          else return meanDBH / 100;
    }

    public double getHeight_Dominance(String dominance, double meanHeight){
          if (dominance.equalsIgnoreCase("dominant" ) ) return meanHeight * 1.05;                             //1
          else if( dominance.equalsIgnoreCase("subDominant") )return meanHeight/1.05;                   //2
          else if( dominance.equalsIgnoreCase("mean") )return meanHeight; //option if actual tree data is used  //9  
          else return meanHeight;
    }
    
    public double pow( double A , double B ){
        //{calculates A raised to the power of B}
        if( A <= 0 ) return 0;
        else return Math.exp(B * Math.log(A));
    }
/*
function ThinningToStr(AThinning : TThinning): String;
begin
  case AThinning of
    thCZ: Result :='CZ';  //'Crown thinning'
    thIF: Result :='IF';  //'Intermediate with 5 year delay'
    thIT: Result :='IT';  //'Intermediate with 10 year delay'
    thIZ: Result :='IZ';  //'Intermediate with no delay'
    thLF: Result :='LF';  //'Line thinning with 5 year delay'
    thLT: Result :='LT';  //'Line thinning with 10 year delay'
    thLZ: Result :='LZ';  //'Line thinning with no delay'
    thNO: Result :='NO';  //'No thinning'
  end;
  //end case
end;
// end ThinningToStr
*/
    public double topToMeanHeight(String species, double TopHeight ){
    // function to retrieve mean tree height for ForestGALES
    // It needs to know the species
        if( species.equalsIgnoreCase("SP") ) return TopHeight * 1.0437 - 1.7304;;
        if( species.equalsIgnoreCase("CP") ) return TopHeight * 1.0316 - 1.3262;
        if( species.equalsIgnoreCase("LP") ) return TopHeight * 1.0244 - 1.2085;
        if( species.equalsIgnoreCase("EL") ) return TopHeight * 1.0671 - 2.2422;
        if( species.equalsIgnoreCase("HL") ) return TopHeight * 1.0348 - 1.236;
        if( species.equalsIgnoreCase("JL") ) return TopHeight * 1.0348 - 1.236;
        if( species.equalsIgnoreCase("DF") ) return TopHeight * 1.0343 - 1.951;
        if( species.equalsIgnoreCase("NS") ) return TopHeight * 1.0643 - 2.6057;
        if( species.equalsIgnoreCase("SS") ) return TopHeight * 1.0467 - 2.1452;
        if( species.equalsIgnoreCase("NF") ) return TopHeight * 1.0465 - 2.3502;
        if( species.equalsIgnoreCase("GF") ) return TopHeight * 1.0213 - 2.1552;
        if( species.equalsIgnoreCase("WH") ) return TopHeight * 1.0245 - 1.515;
        return 0;
    }
    
    public double getCanopyDepth(){
        double result = 0.0;
        if( species.equalsIgnoreCase("SP") ) result = 0.557 *  meanHeight - 1.83;
        else if( species.equalsIgnoreCase("CP") ) result = 0.4118 * meanHeight + 0.6658;
        else if( species.equalsIgnoreCase("LP") ) result = 0.4605 * meanHeight + 1.0356; 
        else if( species.equalsIgnoreCase("EL") ) result =  3.341 * Math.exp(0.0496 * meanHeight);
        else if( species.equalsIgnoreCase("HL") ) result = 0.3423 * meanHeight + 1.9933;
        else if( species.equalsIgnoreCase("JL") ) result = 0.3423 * meanHeight + 1.9933;
        else if( species.equalsIgnoreCase("DF") ) result = 0.5863 * meanHeight - 1.023;
        else if( species.equalsIgnoreCase("NS") ) result = 0.683 *  meanHeight - 1.66;
        else if( species.equalsIgnoreCase("SS") ) result = 0.3489 * meanHeight + 1.7828; 
        else if( species.equalsIgnoreCase("NF") ) result = 0.5228 * meanHeight + 0.2032; 
        else if( species.equalsIgnoreCase("GF") ) result = 0.5205 * meanHeight + 0.6119; 
        else if( species.equalsIgnoreCase("WH") ) result = 0.1636 * meanHeight + 4.7828;           
        
        if( result > meanHeight ) return meanHeight;
        if( result < 0 ) return meanHeight;
        return result;
    }
    
    public double getCanopyBreadth( double currentSpacing ){
        double result = 0.0;
        if( species.equalsIgnoreCase("SP") ) result = -0.097 + 0.146 * meanDBH * 100;
        else if( species.equalsIgnoreCase("CP") ) result = -0.097 + 0.146 * meanDBH * 100;
        else if( species.equalsIgnoreCase("LP") ) result = 0.1022 + 0.2918 * meanDBH * 100; 
        else if( species.equalsIgnoreCase("EL") ) result = 0.191 * meanDBH * 100 - 0.22;
        else if( species.equalsIgnoreCase("HL") ) result = 0.1754 * meanDBH * 100 - 0.4519; 
        else if( species.equalsIgnoreCase("JL") ) result = 0.1754 * meanDBH * 100 - 0.4519; 
        else if( species.equalsIgnoreCase("DF") ) result = 6.4-40 * Math.pow (meanDBH * 100, -0.9);
        else if( species.equalsIgnoreCase("NS") ) result = 0.126 * meanDBH * 100 + 0.3145;
        else if( species.equalsIgnoreCase("SS") ) result = 0.064 + 0.1549 * meanDBH * 100; 
        else if( species.equalsIgnoreCase("NF") ) result = 0.1255 * meanDBH * 100 + 0.3306;   
        else if( species.equalsIgnoreCase("GF") ) result = 0.1255 * meanDBH * 100 + 0.3306;   
        else if( species.equalsIgnoreCase("WH") ) result = 4.5-40 * Math.pow (meanDBH * 100, -1.1); 
        
        if( result > 2.0 * currentSpacing ) result = 2.0 * currentSpacing;
        if( result > meanHeight ) result = meanHeight;

        return result;
    }
        

//{****************** TTree implementation ***********************************}

    public FGTreeU(){ 
        treeCharacteristicsCounter=0;
          // set up the default values
        species = "SS";
        topHeight = 12.0;
        meanDBH = 0.12;
        meanHeight = topToMeanHeight(species, topHeight);
        forceMethod = "Roughness";
        yc = 12;
          //YCString := IntToStr(fYC); 
          //fThinning := thNO;
        //ThinningString := ThinningToStr (fThinning);
        initSpacing = 1.7;
        dominance = "mean";
        snowDepth = 0; // No snow
          //Modified by RAD
        noOfSections = 30;// {10}
         /* (*Tree_Characteristics;  *)
        {to calculate tree characteristics. As properties are initialised internally
          by accessing the private data storage, the Tree_Characteristics procedure
          is not initialised. Therefore, this has to be called directly. Accordingly,
          when properties are accessed using read and write methods, the procedure is
          automatically called to update the Tree Characteristics. }*/
    }
    
    public void setSpecies( String species ){
        this.species = species;
    }
    
    public String getSpecies(){
        return species;
    }
    
    public void setTopHeight( double value ){
        topHeight = value;
        meanHeight = topToMeanHeight( getSpecies(), topHeight );
    }
    
    public double getTopHeight(){
        return topHeight;
    }

    public void setMeanDbh( double value ){
        meanDBH = value;
        if( meanDBH >= 1) meanDBH = meanDBH/100.0;//convert to cm
    }

    public double getMeanDbh(){
        return meanDBH;
    }
    
    public double getMeanHeight(){
        return meanHeight;
    }

/*
procedure TTree.SetMeanDBH(const Value: Single);
begin
  fMeanDBH := Value;
  if fMeanDBH >=1 then fMeanDBH := fMeanDBH/100; // to convert cm to metres
  (*Tree_Characteristics; *)
end;*/


    public void setForceMethod(String forceMethod){
        this.forceMethod = forceMethod;
    }
    
    public void setYC( int value ){
        this.yc = value;
    }
    
    public int getYC(){
        return yc;
    }

/*function TTree.GetThinning: TThinning;
begin
 Result := fThinning;
 ThinningString := ThinningToStr (fThinning); // To obtain a 2-char string in the old Yield Tables method
end;

procedure TTree.SetThinning(const Value: TThinning);
begin
  fThinning := Value;
  ThinningString := ThinningToStr (fThinning);   // To obtain a 2-char string in the old Yield Tables method
end;
*/
    
    public void setDominance(String value){
        this.dominance = value;
          //MeanHeight := Height_Dominance(fDominance, MeanHeight);
          //fMeanDBH :=  DBH_Dominance(fDominance, fMeanDBH);
          //Tree_Characteristics;
    }
    
    public String getDominance(){
        return dominance;
    }
    
    public double getInitSpacing(){
        return initSpacing;
    }
    
    public void setInitSpacing(double initSpacing) {
        this.initSpacing = initSpacing;
    }
    
    public double getSnowDepth(){
        return snowDepth;
    }
    
    public void setSnowDepth(double snowDepth) {
        this.snowDepth = snowDepth;
    }

    public int getNoOfSections() {
        return noOfSections;
    }
    
    public void setNoOfSections(int noOfSections) {
        this.noOfSections = noOfSections;
    }

    public void treeCharacteristics( double spacing ){
/*
{This part of the programme needs Mensurational information and help!!!
 A good start would be to have access to the Yield tables from microFIAP.  This has been
 done but it doesn't give any crown characteristics}
{This procedure is used to calculate Canopy Breath, Canopy Depth, Stem Density
 and Canopy Density.  It takes Species as parameters. }*/

  //int i;//  : Word;        // counter used in the calculation of Branch Volume at each section
  double DCanopyBase;//  : Single;      // Diameter at the bottom of the canopy
  double C1         ;//  : Single;      // C1 is a constant to estimate diameter above Canopy Base
  double C2         ;//  : Single;      // C2 is a constant calculated to measure diameters below MidCanopy
  double DBH_Height ;//  : Single;      // Height at which DBH is meassured

  //XXX count the number of times that routine is called
  treeCharacteristicsCounter++;
  canopyBdth = getCanopyBreadth( spacing );
  //end case

  // Calculate Canopy Depth
  canopyDepth = getCanopyDepth();

  // Calculate Stem Density
  // stemDensity = 850;  // to initialise attribute
  /*case fSpecies of
    spSP..spDF: StemDensity := 850;  {Data from Finland}
    spNS,spSS : StemDensity := 850;  {Average for Sitka spruce in British tree pulling data base}{800 Data from Finland}
    spNF..spWH: StemDensity := 850;  {Data from Finland}
  end;*/
  //end case

  // Calculate Canopy Density
  //canopyDensity = 2.5; //  {This bit needs analysis of Tree Pulling Database}

  // Calculate Canopy Height
  canopyHeight = meanHeight - canopyDepth;

  // Calculate Mid Canopy height
  //{ This is an emprirical model where above MidCanopy the diameter is
  //  constant, while below it the diameter = cubicroot(aZ)}
  midCanopy = meanHeight - (canopyDepth / 2.0);
  // Trap to get rid of small trees. If MidCanopy goes below 1.3 m you cannot measure DBH
  if( midCanopy > 1.3 ) DBH_Height = 1.3; 
  else DBH_Height=0;

  // Calculate Snow Weight
  //{ At present this is a relatively simple model and it's barely used by the model}
  snowWeight = snowDepth * SNOWDENSITY * Math.PI * Math.pow((canopyBdth / 2.0),2);

  // C2 is a constant calculated to measure diameters below MidCanopy
  // Therefore d(Z) = C2*Pow(Z, 0.33)
  C2 = meanDBH / Math.pow ((midCanopy - DBH_Height), 0.333);
  DCanopyBase = C2 * Math.pow ((canopyDepth / 2),0.333);

  // C1 is a constant to estimate diameter above Canopy Base
  // Therefore d(z') = C1* z'  (z'= tree height above Canopy Base
  C1 = DCanopyBase / canopyDepth;

  // Calculate Branch Weight
  branchVolume = 0;
  for (int i = 0 ; i < noOfSections; i++)
  {
      Z[i] = i * meanHeight / noOfSections;
      if ( Z[i] < canopyHeight ) Diam[i] = C2 * Math.pow((midCanopy - Z[i]), 0.333);
        //assumes constant stress for stem below crown and loading at mid canopy
      else Diam[i] = C1 * (meanHeight - Z[i]);
      // assumes linear change of diameter in crown after Mattheck
      if( Z[i] < canopyHeight ) branchWidth[i] = 0;
      else if (Z[i] < midCanopy) branchWidth[i] = canopyBdth * (Z[i] - canopyHeight) / (midCanopy - canopyHeight);
      else branchWidth[i] = canopyBdth * (1 - (Z[i] - midCanopy)/(meanHeight - midCanopy));
      branchVolume = branchVolume + Math.PI * (meanHeight / noOfSections) * Math.pow (branchWidth[i] / 2.0, 2.0);
      // #36MARK: If BranchVolume is zero then cause a divide by zero error later on :-( 
  }
      // end for
  
  //sb prevent divide by zero
  if( branchVolume < 0.1 ) branchVolume = 0.1;
  
  
  branchWeight = branchVolume * canopyDensity;//slight difference here

  // Calculate Stem Weight
  stemWeight = 0;
  for(int i= 0; i < noOfSections; i++ ){
     MSnow[i] = (Math.PI * (meanHeight / noOfSections) * Math.pow(branchWidth[i] / 2.0, 2.0))* snowWeight / branchVolume;
     mass[i] = ( stemDensity * Math.PI * (Diam[i] / 2) * (Diam[i] / 2)
             + canopyDensity * Math.PI * (branchWidth[i] / 2) * (branchWidth[i] / 2)) * meanHeight / noOfSections + MSnow[i];
     stemWeight = stemWeight + stemDensity * Math.PI * (Diam[i] / 2) * (Diam[i] / 2) * meanHeight / noOfSections;
  }
    // end for
  for( int i = 0 ; i < noOfSections; i ++ ) mass[i] = mass[i] * noOfSections / meanHeight;
// end  Tree_Characteristics
  }
}
