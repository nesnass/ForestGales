package uk.gov.forestresearch.forestmodels.gales;

//import uk.gov.forestresearch.applications.emis.ForestGalesMapper;
//import uk.gov.forestresearch.forestobjects.Soil;       
//import uk.gov.forestresearch.models.soil.SoilVo;

public class ForestGalesTreeMechanics {
    
    private static int    SECTIONS               = 200;
    private static int MAX_NO_OF_TREE_SECTIONS   = 50;
    private static double [][][] multiplierArray = new double[5][3][3];
    private static double [][][] maxWTArray = new double[5][3][3];
    private static double [] windProfileArray = new double [SECTIONS];
    private static int noOfSections = 30;
    
    static 
    {
        //PINE
        multiplierArray [0][0][0] = 100.0;
        multiplierArray [0][0][1] = 94.9;
        multiplierArray [0][0][2] = 92.9;
        
        multiplierArray [0][1][0] = 94.0;
        multiplierArray [0][1][1] = 89.3;
        multiplierArray [0][1][2] = 87.4;
            
        multiplierArray [0][2][0] = 98.0;
        multiplierArray [0][2][1] = 93.0;
        multiplierArray [0][2][2] = 86.3;
        
        //LODGEPOLE
        multiplierArray [1][0][0] = 100.0;
        multiplierArray [1][0][1] = 89.5;
        multiplierArray [1][0][2] = 92.0;
        
        multiplierArray [1][1][0] = 94.4;
        multiplierArray [1][1][1] = 84.4;
        multiplierArray [1][1][2] = 84.4;
        
        multiplierArray [1][2][0] = 100.0;
        multiplierArray [1][2][1] = 89.5;
        multiplierArray [1][2][2] = 92.0;
        
        //FIR
        multiplierArray [2][0][0] = 100.0;
        multiplierArray [2][0][1] = 94.9;
        multiplierArray [2][0][2] = 92.9;
        
        multiplierArray [2][1][0] = 84.6;
        multiplierArray [2][1][1] = 80.2;
        multiplierArray [2][1][2] = 78.6;
        
        multiplierArray [2][2][0] = 98.0;
        multiplierArray [2][2][1] = 93.0;
        multiplierArray [2][2][2] = 86.3;
        
        //SPRUCE
        multiplierArray [3][0][0] = 100.0;
        multiplierArray [3][0][1] = 90.9;
        multiplierArray [3][0][2] = 89.4;
        
        multiplierArray [3][1][0] = 78.3;
        multiplierArray [3][1][1] = 66.8;
        multiplierArray [3][1][2] = 65.7;
        
        multiplierArray [3][2][0] = 98.0;
        multiplierArray [3][2][1] = 96.2;
        multiplierArray [3][2][2] = 96.2;
        
        //OTHER
        multiplierArray [4][0][0] = 100.0;
        multiplierArray [4][0][1] = 94.9;
        multiplierArray [4][0][2] = 92.9;
        
        multiplierArray [4][1][0] = 94.0;
        multiplierArray [4][1][1] = 89.3;
        multiplierArray [4][1][2] = 87.4;
        
        multiplierArray [4][2][0] = 98.0;
        multiplierArray [4][2][1] = 93.3;
        multiplierArray [4][2][2] = 86.3;
               
        
        //PINE
        maxWTArray [0][0][0] = 576.0;
        maxWTArray [0][0][1] = 474.0;
        maxWTArray [0][0][2] = 244.0;
        
        maxWTArray [0][1][0] = 0;
        maxWTArray [0][1][1] = 0;
        maxWTArray [0][1][2] = 0;
            
        maxWTArray [0][2][0] = 0;
        maxWTArray [0][2][1] = 0;
        maxWTArray [0][2][2] = 0;
        
        //LODGEPOLE
        maxWTArray [1][0][0] = 895.0;
        maxWTArray [1][0][1] = 378.0;
        maxWTArray [1][0][2] = 291.0;
        
        maxWTArray [1][1][0] = 374.0;
        maxWTArray [1][1][1] = 0;
        maxWTArray [1][1][2] = 0;
        
        maxWTArray [1][2][0] = 484.0;
        maxWTArray [1][2][1] = 0;
        maxWTArray [1][2][2] = 0;
        
        //FIR
        maxWTArray [2][0][0] = 515.0;
        maxWTArray [2][0][1] = 0;
        maxWTArray [2][0][2] = 0;
        
        maxWTArray [2][1][0] = 347;
        maxWTArray [2][1][1] = 0;
        maxWTArray [2][1][2] = 0;
        
        maxWTArray [2][2][0] = 0;
        maxWTArray [2][2][1] = 0;
        maxWTArray [2][2][2] = 0;
        
        //SPRUCE
        maxWTArray [3][0][0] = 839.0;
        maxWTArray [3][0][1] = 429.0;
        maxWTArray [3][0][2] = 0;
        
        maxWTArray [3][1][0] = 1366;
        maxWTArray [3][1][1] = 2068;
        maxWTArray [3][1][2] = 725;
        
        maxWTArray [3][2][0] = 1070;
        maxWTArray [3][2][1] = 1013;
        maxWTArray [3][2][2] = 339;
        
        //OTHER
        maxWTArray [4][0][0] = 0;
        maxWTArray [4][0][1] = 0;
        maxWTArray [4][0][2] = 0;
        
        maxWTArray [4][1][0] = 0;
        maxWTArray [4][1][1] = 0;
        maxWTArray [4][1][2] = 0;
        
        maxWTArray [4][2][0] = 0;
        maxWTArray [4][2][1] = 0;
        maxWTArray [4][2][2] = 0;
               
    }
    
    /** Creates a new instance of ForestGalesTreeMechanics */
    public ForestGalesTreeMechanics() {
        brownEdge = false;
        edge      = 0.0;
        noDataFlag = false;
        beyondRangeFlag = false;
    }

    public ForestGalesTreeMechanics( String cultivation, String soil, String treeSpecies, String drainage,  double dams, double meanHeight, double canopyBreadth, double canopyWidth, double spacing, double gap ) {
        //if( edge > 0.0 )brownEdge = false;
        //setBrownEdge( edge != 0.0 );
        //this.edge = 93.736801147;
        setBrownEdge( gap == 0.0 );
        this.dams = dams;
        this.gapWidth = gap;
        this.meanHeight = meanHeight;
        this.cultivation = cultivation;
        this.soil = soil;
        this.species = treeSpecies;
        this.drainage = drainage;
        initialiseAWeibull();
        calculateEdge();
       // roughnessSimple( canopyBreadth, canopyWidth, spacing );
    }
    
    
    
    public ForestGalesTreeMechanics( String cultivation, String soil, String treeSpecies, String drainage,  double dams, double meanHeight, double canopyBreadth, double canopyWidth, double spacing ) {
        this();
        this.dams = dams;
        this.gapWidth = meanHeight * 10;
        this.meanHeight = meanHeight;
        this.cultivation = cultivation;
        this.soil = soil;
        this.species = treeSpecies;
        this.drainage = drainage;
        initialiseAWeibull();
        calculateEdge();
       // roughnessSimple( canopyBreadth, canopyWidth, spacing );
    }
    
    public void doCalculations(double currentSpacing, double stemWeight, double diameter, double breadth, double depth , double [] diam, double [] Z, double [] mass, double meanDBH){
        initialiseAWeibull();
        calculateCriticalWind(currentSpacing, stemWeight, diameter , breadth , depth, diam, Z, mass, meanDBH);
        probOfBreak = getAnnualExceedenceProbability( U_Break_10 );
        probOfOverturn = getAnnualExceedenceProbability( U_Overturn_10 );
    }
    
    public void doCalculations(double currentSpacing, double stemWeight, double diameter, double breadth, double depth , double [] diam, double [] Z, double [] mass, double meanDBH, double a_weibull, double k_weibull){
        this.a_weibull = a_weibull;
        this.k_weibull = k_weibull;
        calculateCriticalWind(currentSpacing, stemWeight, diameter , breadth , depth, diam, Z, mass, meanDBH);
        probOfBreak = getAnnualExceedenceProbability( U_Break_10 );
        probOfOverturn = getAnnualExceedenceProbability( U_Overturn_10 );
    }    
    
    public boolean getBrownEdge(){
        //calculateEdge( brownEdge );
        return brownEdge;
    }
    
    private void calculateEdge( ){
        edge = TREE_HEIGHTS_FROM_EDGE * meanHeight;
    }
    public void setBrownEdge( boolean value ){
        this.brownEdge = value;
        if( brownEdge )
            calculateEdge();
        else
            edge = 0.0;
    }
    
    public double getGapWidth(){
        return gapWidth;
    }
    
    public void setGapWidth( double value ){
        gapWidth = value;
         if( gapWidth > 10 * meanHeight )
            gapWidth = 10 * meanHeight;       
        calculateEdge();
    }
    
    public void checkEdgeAndGap(){
        if( edge > TREE_HEIGHTS_FROM_EDGE * meanHeight )
            calculateEdge();
        if( gapWidth > BIGGAP * meanHeight )
            gapWidth = BIGGAP * meanHeight;
    }
    
    public double getMeanGapFactor( double gapSize, double treeHeight ){
        return getGapFactor( gapSize, treeHeight, 0.001, 0.562 );
    }
    
    public double getMaxGapFactor(  double gapSize, double treeHeight ){
        return getGapFactor( gapSize, treeHeight, 0.0064, 0.3467 );
    }
    
    public double getGapFactor(  double gapSize, double treeHeight , double param1, double param2){
        if( gapSize > BIGGAP * treeHeight ) gapSize = BIGGAP * treeHeight;
        double X_H = gapSize / treeHeight ;
        double gapTen = param1 * Math.pow( BIGGAP , param2 );
        return ( param1 * Math.pow( X_H , param2 ))/gapTen;        
    }    
    
    private int getCultivationScore(){ // move these properties to the cultivation XML file
        if( cultivation.equalsIgnoreCase( "Notched" ) )
            return 1;
        if( cultivation.equalsIgnoreCase( "Complete Ploughing" ) )
            return 1;
        if( cultivation.equalsIgnoreCase( "Shallow Ploughing < 45cm" ) )
            return 2;
        if( cultivation.equalsIgnoreCase( "Deep Ploughing > 45cm" ) )
            return 3;
        if( cultivation.equalsIgnoreCase( "Scarifying" ) )
            return 2;
        if( cultivation.equalsIgnoreCase( "Disc Trenching" ) )
            return 3;
        if( cultivation.equalsIgnoreCase( "Turf Planting" ) )
            return 1;
        if( cultivation.equalsIgnoreCase( "Moling" ) )
            return 1;
        if( cultivation.equalsIgnoreCase( "Alterate Single/Double" ) )
            return 3;
        if( cultivation.equalsIgnoreCase( "Contour Ploughing" ) )
            return 3;
        if( cultivation.equalsIgnoreCase( "Mound planting" ) )
            return 1;        
        return 0;
    }
    
    private int getSoilScore(){ // move these properties to the soil XML file
        if( soil.equalsIgnoreCase("Brown Earth")) return 1;
        if( soil.equalsIgnoreCase("Intergrade (Brown Earth-Ironpan)")) return 1;
        if( soil.equalsIgnoreCase("Podzol")) return 1;
        if( soil.equalsIgnoreCase("Iron Pan")) return 1;
        if( soil.equalsIgnoreCase("Intergrade (Brown Earth-Gley)")) return 2;
        if( soil.equalsIgnoreCase("Peaty Gley"))  return 2;
        if( soil.equalsIgnoreCase("Gley")) return 2;
        if( soil.equalsIgnoreCase("Flushed Basin Peat")) return 3;
        if( soil.equalsIgnoreCase("Flushed Blanket Peat")) return 3;
        if( soil.equalsIgnoreCase("Unflushed Sphagnum Bog")) return 3;
        if( soil.equalsIgnoreCase("Unflushed Blanket Bog")) return 3;
        if( soil.equalsIgnoreCase("Calcareous Soil")) return 1;
        if( soil.equalsIgnoreCase("Skeletal Soil")) return 2;
        if( soil.equalsIgnoreCase("Eroded Bog")) return 3;
        if( soil.equalsIgnoreCase("Littoral Soil")) return 2;
        return 0;
    }
    
    private int getSpeciesIndex(){
        if( species.equalsIgnoreCase("SP") ) return 1;
        if( species.equalsIgnoreCase("CP") ) return 1;
        if( species.equalsIgnoreCase("LP") ) return 2;
        if( species.equalsIgnoreCase("EL") ) return 5;
        if( species.equalsIgnoreCase("HL") ) return 5;
        if( species.equalsIgnoreCase("JL") ) return 5;
        if( species.equalsIgnoreCase("DF") ) return 5;
        if( species.equalsIgnoreCase("NS") ) return 4;
        if( species.equalsIgnoreCase("SS") ) return 4;
        if( species.equalsIgnoreCase("VPSS") ) return 4;
        if( species.equalsIgnoreCase("QSS") ) return 4;
        if( species.equalsIgnoreCase("NF") ) return 3;
        if( species.equalsIgnoreCase("GF") ) return 3;
        if( species.equalsIgnoreCase("WH") ) return 5;
        return 0;
    }
    
    private double getSpeciesFreeNotch(){
        if( species.equalsIgnoreCase("SP") ) return 140.1;
        if( species.equalsIgnoreCase("CP") ) return 140.1;
        if( species.equalsIgnoreCase("LP") ) return 140.0;
        if( species.equalsIgnoreCase("EL") ) return 176.9;
        if( species.equalsIgnoreCase("HL") ) return 176.9;
        if( species.equalsIgnoreCase("JL") ) return 176.9;
        if( species.equalsIgnoreCase("DF") ) return 174.7;
        if( species.equalsIgnoreCase("NS") ) return 166.7;
        if( species.equalsIgnoreCase("SS") ) return 166.7;
        if( species.equalsIgnoreCase("VPSS") ) return 166.7;
        if( species.equalsIgnoreCase("QSS") ) return 166.7;
        if( species.equalsIgnoreCase("NF") ) return 180.0;
        if( species.equalsIgnoreCase("GF") ) return 180.0;
        if( species.equalsIgnoreCase("WH") ) return 159.3;
        return 0;        
    }
    
    
    public double calculateMaxOverturningMoment( double stemWeight){
        int cultIdx = getCultivationScore();
        int soilIdx = getSoilScore();
        int specIdx = getSpeciesIndex();
        double notch   = getSpeciesFreeNotch();
        double maxWt   = maxWTArray [ specIdx- 1][soilIdx-1][cultIdx-1 ];
        if( maxWt == 0.0 ) noDataFlag = true;
        else if( stemWeight > maxWt ) beyondRangeFlag = true;
        double RM = notch * multiplierArray[ specIdx-1][soilIdx-1][cultIdx-1 ] / 100;
        double overturnMoment = RM * stemWeight;
        if( drainage.equalsIgnoreCase( "average") ) return overturnMoment;
        else if( drainage.equalsIgnoreCase( "poor") ) return overturnMoment * 0.8;
        else return overturnMoment * 1.2;
    }
    
    public double calculateBreakingBM( double diameter){
        return calculateMOR() * Math.PI * Math.pow( diameter, 3.0)/32.0;
    }
    
    public double calculateMOR(){
        if( species.equalsIgnoreCase("SP") ) return 4.6E7 * 0.85;
        if( species.equalsIgnoreCase("CP") ) return 4.1E7 * 0.85;
        if( species.equalsIgnoreCase("LP") ) return 4.1E7 * 0.8;
        if( species.equalsIgnoreCase("EL") ) return 5.3E7 * 0.85;
        if( species.equalsIgnoreCase("HL") ) return 4.3E7 * 0.85;
        if( species.equalsIgnoreCase("JL") ) return 4.8E7 * 0.85;
        if( species.equalsIgnoreCase("DF") ) return 5.3E7 * 1.0;
        if( species.equalsIgnoreCase("NS") ) return 3.6E7 * 0.9;
        if( species.equalsIgnoreCase("SS") ) return 3.4E7 * 1.0;
        if( species.equalsIgnoreCase("VPSS") ) return 3.4E7 * 1.0;
        if( species.equalsIgnoreCase("QSS") ) return 3.4E7 * 1.0;
        if( species.equalsIgnoreCase("NF") ) return 3.5E7 * 1.0;
        if( species.equalsIgnoreCase("GF") ) return 3.4E7 * 1.0;
        if( species.equalsIgnoreCase("WH") ) return 4.1E7 * 1.0;
        return 0;     
    }
    
    public double calculateMOE(){
        if( species.equalsIgnoreCase("SP") ) return 7.3E9;
        if( species.equalsIgnoreCase("CP") ) return 7.0E9;
        if( species.equalsIgnoreCase("LP") ) return 6.4E9;
        if( species.equalsIgnoreCase("EL") ) return 7.9E9;
        if( species.equalsIgnoreCase("HL") ) return 5.9E9;
        if( species.equalsIgnoreCase("JL") ) return 6.8E9;
        if( species.equalsIgnoreCase("DF") ) return 8.3E9;
        if( species.equalsIgnoreCase("NS") ) return 6.3E9;
        if( species.equalsIgnoreCase("SS") ) return 5.9E9;
        if( species.equalsIgnoreCase("VPSS") ) return 5.9E9;
        if( species.equalsIgnoreCase("QSS") ) return 5.9E9;
        if( species.equalsIgnoreCase("NF") ) return 5.7E9;
        if( species.equalsIgnoreCase("GF") ) return 5.7E9;
        if( species.equalsIgnoreCase("WH") ) return 8.9E9;
        return 0;     
    }
    
    public double calculateWindProcess( double criticalMoment, double stopLimit, double canopyBrdth, double canopyDepth, double currentSpacing, double [] Diam,  double [] Z , double [] Mass, double meanDBH ){
        
        double guessWindSpeed = 64;  // Guess 64 m/s
        double delta = guessWindSpeed / 2.0;
        double forceOnTree = 0.0;
        
        calculateWindProfile( currentSpacing );//loop invariant
        do
        {
            // Calculate porosity
            /*
             * suspected loop invariant method call
             *
             * sb
             */
            porosity = calculateStreamLining(guessWindSpeed);//
            // Calculate Roughness (Z0), Zero Plane Displacement (D) and GammaSolved
            
            
            roughnessSimple( canopyBrdth, canopyDepth, currentSpacing);//
            //Roughness_Simple (CanopyBdth, Porosity, CanopyDepth, , AMeanHeight);
            // calculate the wind profile U_UH
            // CalculateWindProfile(AMeanHeight, ACurrentSpacing, Z0, D);
            
            /*
             * suspected loop invariant method call
             *
             * sb
             */            
            calculateWindProfile( currentSpacing );
            
            // Calculate Force on tree and Height of Force.  These parameters will be passed to
            // the function that calculates the Bending Moment
            HeightOfForce = 0;
            // Calculate Force on tree
            forceOnTree = calculateForce(guessWindSpeed, currentSpacing);

            // Calculate Bending Moment.
            //BendingMoment := CalculateBendingMoment(ForceOnTree, HeightOfForce, AMeanHeight, bugger,
                           //  NoOfSections, Z, Diam, Mass);
            bendingMoment = calculateBendingMoment(forceOnTree, HeightOfForce, Diam, Z, Mass, meanDBH );
            
            //is this some sort of binary search condition???
            if ( bendingMoment > criticalMoment )guessWindSpeed = guessWindSpeed - delta;
            else guessWindSpeed = guessWindSpeed + delta;
            delta = delta  /2.0;
        }
        while (delta < stopLimit == false );
        return guessWindSpeed;
    }
    
    public double calculateStreamLining( double windSpeed ){
        double C = 2.35;
        double N = 0.51;
        if( species.equalsIgnoreCase("SP") ) { C=3.07; N=0.75;}
        if( species.equalsIgnoreCase("CP") ) { C=2.57; N=0.61;}
        if( species.equalsIgnoreCase("LP") ) { C=2.48; N=0.63;}
        if( species.equalsIgnoreCase("EL") ) { C=3.07; N=0.75;}
        if( species.equalsIgnoreCase("HL") ) { C=3.07; N=0.75;}
        if( species.equalsIgnoreCase("JL") ) { C=3.07; N=0.75;}
        if( species.equalsIgnoreCase("DF") ) { C=2.4; N=0.7;}
        if( species.equalsIgnoreCase("NS") ) { C=2.35; N=0.51;}
        if( species.equalsIgnoreCase("SS") ) { C=2.35; N=0.51;}
        if( species.equalsIgnoreCase("NF") ) { C=4.7; N=0.74;}
        if( species.equalsIgnoreCase("GF") ) { C=4.7; N=0.74;}
        if( species.equalsIgnoreCase("WH") ) { C=1.51; N=0.68;}
        if( windSpeed < 10) windSpeed = 10;
        if( windSpeed > 25) windSpeed = 25;
        return C * Math.pow( windSpeed, -1 * N );
    }
    
    public void roughnessSimple( double canopyBdth, double canopyDepth, double currentSpacing){
        double CD1 = 7.5;
        double canopyBreadth = canopyBdth * porosity/2.0 ;
        double lambda = (canopyBreadth * canopyDepth)/ (currentSpacing * currentSpacing);
        double lambdaCapital = 2 * lambda;
        double psih = Math.log( CW ) - 1 + 1/CW;
        D = ( 1 - (( 1 - Math.exp( -Math.sqrt( CD1 * lambdaCapital)))  /  Math.sqrt( CD1 * lambdaCapital))) * meanHeight;
        if( lambdaCapital > 0.6 ) gammaSolved = 1/Math.sqrt( CS + CR * 0.3 );
        else gammaSolved = 1.0 /Math.sqrt( CS + CR * lambdaCapital/2.0 );
        z0 = (meanHeight - D)* Math.exp(( -K * gammaSolved) + psih);
    }
    
    public void calculateWindProfile( double currentSpacing ){
        double S_H = currentSpacing/ meanHeight;
        for( int i = 0 ; i < SECTIONS/2; i++ )
        {
            U_UH[i] = Math.exp( -( -2.4 * Math.log(S_H) + 1.621 ) * ( 1.0 - (i/(SECTIONS/2.0))) );
        }
        for( int i = SECTIONS/2 ; i < SECTIONS ; i++)
        {
            /*double a = i * meanHeight;
            double b = SECTIONS/2.0;
            double c = a/(b -D);
            double d = Math.log( c) /z0;
            double e = Math.log( meanHeight - D/z0);
            U_UH[i]  = d/e;*/
            
            U_UH[i] = Math.log( ( (i * meanHeight/(SECTIONS/2.0)) - D)/z0 ) / Math.log(( meanHeight - D)/z0);
        }
    }
    
    public double calculateForce( double windSpeed , double currentSpacing ){
        HeightOfForce = D ;
        return  RO *  (currentSpacing * windSpeed / gammaSolved) * (currentSpacing * windSpeed / gammaSolved )  ;
    }

    public double calculateBendingMoment( double forceOnTree, double heightOfForce, double [] Diam, double [] Z, double [] Mass, double meanDBH ){
        double K = 0.0;   //  {root bending term. See paper by Neild and Wood}
/*var
  Slope   :single;
  R       :single;
  Fac1    :single;
  Fac2    :single;
  Fac1Max :single;
  Fac2Max :single;
  Eq      :single;
  MaxEq   :single;
  ii, iii :Word;    // counters
  Finish  :Boolean;
  I       :array[0..(MAX_NO_OF_TREE_SECTIONS - 1)] of Single;  {second area moment of inertia}
  Z_H     :array[0..(MAX_NO_OF_TREE_SECTIONS - 1)] of Single;  {ratio of height from top of tree to tree height}
  YOld    :array[0..(MAX_NO_OF_TREE_SECTIONS - 1)] of Single;
  MX      :array[0..(MAX_NO_OF_TREE_SECTIONS - 1)] of Single;
  M_EI    :array[0..(MAX_NO_OF_TREE_SECTIONS - 1)] of Single;
  Mz_EI   :array[0..(MAX_NO_OF_TREE_SECTIONS - 1)] of Single;
  Y       :array[0..(MAX_NO_OF_TREE_SECTIONS - 1)] of single; {displacement with height}
  MG      : Array [0..50] of Single;
  MP      : Array [0..50] of Single;
  //MTotal  : Array [0..50] of Single;*/

  //if d too close to the top of the tree use a default height for the action of the wind
  if ( heightOfForce > 0.8 * meanHeight )heightOfForce = 0.8 * meanHeight;
  // initialise ii
  double Slope = 0.0;
  double R     = 0.0;
  double fac1   = 0.0;
  double fac2    = 0.0;
  double fac1Max = 0.0;
  double fac2Max = 0.0;
  double eq      = 0.0;
  double maxEq   = 0.0;
  boolean Finish = false;        
  double [] Y = new double[  MAX_NO_OF_TREE_SECTIONS ];  //{displacement with height}  
  double [] I = new double[  MAX_NO_OF_TREE_SECTIONS ];//  {second area moment of inertia}
  double [] Z_H = new double[  MAX_NO_OF_TREE_SECTIONS ];//  {ratio of height from top of tree to tree height}
  double [] YOld = new double[  MAX_NO_OF_TREE_SECTIONS ];
  double [] MX   = new double[  MAX_NO_OF_TREE_SECTIONS ];
  double [] M_EI = new double[  MAX_NO_OF_TREE_SECTIONS ];
  double [] Mz_EI= new double[  MAX_NO_OF_TREE_SECTIONS ];
  double [] MG   = new double [51];
  double [] MP   = new double [51];
  double [] MTotal = new double[51];
 
  for ( int i = 0 ; i < noOfSections; i++) Y[i] = 0.0;
  // Assign values to R, Fac1Max, Fac2Max and MaxEq
  R = (meanHeight-heightOfForce)/meanHeight;
  fac1Max = Math.pow(R, 0.6);
  fac2Max = Math.pow(R, -0.4);
  maxEq= (2.5 * R - 4.17 * fac1Max - 0.71 * R * R - 1.79 * R * fac2Max + 1.67 + 2.5 * R);//

  for (int ii = 0; ii < noOfSections ; ii++ ){
      /*if( ii == 29 )
          System.out.println( "in bad sector" );*/
      Z_H[ii] = (meanHeight - Z[ii]) / meanHeight;
      I[ii] = Math.PI * Math.pow (Diam[ii], 4) / 64.0;
      fac1 = Math.pow(Z_H[ii], 0.6);
      fac2 = Math.pow(Z_H[ii], -0.4);
      eq = (2.5 * Z_H[ii] - 4.17 * fac1 - 0.71 * Z_H[ii] * R - 1.79 * R * fac2 + 1.67 + 2.5 * R);
      if ( Z[ii] < heightOfForce )
        Y[ii] = forceOnTree * meanHeight * meanHeight * meanHeight * eq / (MOE * I[0]);
      else
      {
        if( (int)Math.rint(heightOfForce * noOfSections / meanHeight) > 0 ){
        Slope = (Y[(int)Math.rint(heightOfForce * noOfSections / meanHeight)] -
                 Y[(int)Math.rint(heightOfForce * noOfSections / meanHeight) - 1]) /
                 (Z[(int)Math.rint(heightOfForce * noOfSections / meanHeight)] -
                 Z[(int)Math.rint(heightOfForce * noOfSections / meanHeight) - 1]);
        Y[ii] = (forceOnTree * meanHeight * meanHeight * meanHeight * maxEq /
                 (MOE * I[0])) + Slope * (Z[ii] - heightOfForce);
        }
      }
  // first guess at tree bending based on Gardiner 1989
     if( Y[ii] > (Z[ii] * Math.sin(60.0 * Math.PI / 180.0)) )
         Y[ii] = (Z[ii] * Math.sin(60 * Math.PI / 180)); //stops tree bending more than 30 degs
  }
 // end for ii
  //ok to here
    do{//while ( Finish == false){
    Finish = true;
    for( int ii= 0 ; ii < (noOfSections); ii++ ){
        MG[ii] = 0;
        YOld[ii] = Y[ii];
        MX[ii] = Mass[ii] * YOld[ii];
        Y[ii] = 0;
    }
    // double loop
    for( int ii = 0; ii < (noOfSections); ii++ )
        for( int iii = ii ; iii< (noOfSections); iii++ )
            MG[ii] = MG[ii] + (MX[iii] - YOld[ii] * Mass[iii]) * G * (meanHeight / noOfSections);
    //ok to here
    for( int ii= 0 ; ii < (noOfSections) ; ii++ ){
        if( Z[ii] < heightOfForce )  MP[ii] = forceOnTree * (heightOfForce - Z[ii]);
        else MP[ii]=0;
        
        MTotal[ii] = MG[ii] + MP[ii];
        
        if ( Diam[ii] > 0  ) M_EI[ii] =  MTotal[ii] * 64.0 / (MOE * Math.PI * Math.pow(Diam[ii], 4.0));
        else M_EI[ii] = 0;
        Mz_EI[ii] = M_EI[ii] * Z[ii];
    }

    for( int ii = 0; ii <(noOfSections); ii++ )
      for (int iii =0 ; iii <= ii; iii++ )
        Y[ii] = Y[ii] + (Z[ii] * M_EI[iii] - Mz_EI[iii]) * (meanHeight / noOfSections);

    for( int ii = 0; ii <(noOfSections); ii++ )
      Y[ii] = Y[ii] + Z[ii] * MTotal[0] * K * meanHeight * 64.0 / (MOE * Math.PI * Math.pow (meanDBH, 4.0));

     for( int ii = 0; ii <(noOfSections); ii++ )
     {
       if( Y[ii] > (Z[ii] * Math.sin(60 * Math.PI / 180.0)) )
           Y[ii] = (Z[ii] * Math.sin(60 * Math.PI / 180.0)); //{stops tree bending more than 30 degs}
       //System.out.println( "Y = " + Y[ii] + " : YOld = " + YOld[ii] + " : Diff = "+ Math.abs(Y[ii] - YOld[ii]) );
       if( Math.abs(Y[ii] - YOld[ii]) > Y[ii] / 100.0 )
         Finish = false;//false;
     }
    }while( Finish == false );
    return MTotal[0];

    }
    
    public double elevate( double  uhSpeed, double AZ0value, double ADValue, double meanHeight ){
        return uhSpeed * Math.log ( 10/ AZ0value) / Math.log( (meanHeight - ADValue)/AZ0value );
    }
    
    public double edgeSpeed( double uhSpeed10, double AZ0value, double ADValue, double meanHeight ){
        return ( uhSpeed10/ Math.log( CALC_HT/AZ0value)) * Math.log((1000-ADValue)/AZ0value ) 
               * Math.log( 1/FIELDZ0)/Math.log( 1000/(FIELDZ0 * meanHeight) );
    }
    
    public void initialiseAWeibull(){
        a_weibull = -0.9626 + ( 0.4279 * dams ); 
    }
    
    public void doCalculations(){
    }
    
    public double calculateGustiness( double spacing, double treeHeight, double distanceToEdge, double gap){
        if( distanceToEdge > TREE_HEIGHTS_FROM_EDGE * treeHeight )
            distanceToEdge = TREE_HEIGHTS_FROM_EDGE * treeHeight;
        double S_H = spacing/treeHeight;
        if( S_H < 0.075) S_H = 0.075;
        if( S_H > 0.55 ) S_H = 0.55;
        
        double A = -2.1 * S_H + 0.91;
        if( A < 0) A = 0;
        
        double B = 1.0611 * Math.log( S_H ) + 4.2;
        
        double meanBMin = (0.68 * S_H - 0.0385) + ( -0.68 * S_H + 0.4785 ) * Math.pow( 1.7239 * S_H + 0.0316 , TREE_HEIGHTS_FROM_EDGE);
        double maxBMin  = (2.7193 * S_H - 0.061) + ( -1.273 * S_H + 0.9701 ) * Math.pow( 1.1127 * S_H + 0.0311 , TREE_HEIGHTS_FROM_EDGE);
        
        double meanBMEdge = (0.68 * S_H - 0.0385) + ( -0.68 * S_H + 0.4785 ) * Math.pow( 1.7239 * S_H + 0.0316 , 0 );
        double maxBMEdge  = (2.7193 * S_H - 0.061) + ( -1.273 * S_H + 0.9701 ) * Math.pow( 1.1127 * S_H + 0.0311 , 0);
        
        double meanBMEdgeGap = (0.68 * S_H - 0.0385) + ( -0.68 * S_H + 0.4785 ) * Math.pow( 1.7239 * S_H + 0.0316 , 0 ) * getMeanGapFactor( gap, treeHeight );
        double maxBMEdgeGap  = (2.7193 * S_H - 0.061) + ( -1.273 * S_H + 0.9701 ) * Math.pow( 1.1127 * S_H + 0.0311 , 0 ) * getMaxGapFactor( gap, treeHeight );
        
        double oldGustIn   = maxBMin / meanBMin;
        double oldGustEdge = maxBMEdge / meanBMEdge ;
        double oldGustEdgeGap = maxBMEdgeGap / meanBMEdgeGap;
        
        double newGustIn   = ( A * TREE_HEIGHTS_FROM_EDGE ) + B ;
        double newGustEdge = ( A * 0 ) + B;
        
        double newGustEdgeGap = 0.0;
        if( oldGustIn - oldGustEdge > 0 )
            newGustEdgeGap = (( oldGustEdgeGap - oldGustEdge) * (newGustIn - newGustEdge)) / (oldGustIn - oldGustEdge ) + newGustEdge;
        else
            newGustEdgeGap = newGustEdge;
        
        return (((distanceToEdge/ ( treeHeight * TREE_HEIGHTS_FROM_EDGE )) * ( newGustIn - newGustEdgeGap )) +  newGustEdgeGap) * 1.5;
    }
    
    public double calculateEdgeFactor( double spacing, double treeHeight, double distanceToEdge, double gap){

/*{ This function calculates how the mean loading on a tree changes as a function of
  tree height, spacing, distance from edge and size of any upwind gap.  An upwind
  gap greater than 10 tree heights is assumed to be an infinite gap.
  The function calculates the mean and maximum Bending Moment.  The gustiness
  is derived from the ratio of the maximum to the mean loading. The output is used
  to modify the calculated wind loading on the tree which assumes a steady wind and
  a position well inside the forest.}*/

       double Mean_BM;
       double Mean_BM_MidForest;
       double S_H;   // Ratio Spacing and Tree Height


  if( distanceToEdge >  TREE_HEIGHTS_FROM_EDGE * treeHeight ){
    distanceToEdge = TREE_HEIGHTS_FROM_EDGE * treeHeight;
  }
  // calculate Ratio Spacing and Tree Height
  S_H = spacing / treeHeight;
  // S_H must be within 0.075 and 0.55
  if (S_H < 0.075) S_H=0.075;
  if (S_H > 0.55) S_H = 0.55;
  // calculate Mean Bending Moment
  Mean_BM = (0.68 * S_H - 0.0385) + (-0.68 * S_H + 0.4785)
        * Math.pow ((1.7239 * S_H + 0.0316), (distanceToEdge / treeHeight))
        * getMeanGapFactor (gap, treeHeight);
  // calculate Mean Bending Moment at the centre of the forest
  Mean_BM_MidForest = (0.68 * S_H - 0.0385) + (-0.68 * S_H + 0.4785)
        * Math.pow ((1.7239 * S_H + 0.0316), TREE_HEIGHTS_FROM_EDGE)
        * getMeanGapFactor (gap, treeHeight);
  //Accounts for increase in wind loading at forest edge and gap size
       return Mean_BM / Mean_BM_MidForest;        
    }
    
    public double getAnnualExceedenceProbability( double windSpeed ){
        double  Ua = 5;
        double U_C = -0.5903 * Math.pow(k_weibull, 3) + 4.4345 * Math.pow(k_weibull, 2) - 11.8633 * k_weibull + 13.569;
        double  U  = (a_weibull * U_C) * (a_weibull * U_C);
        double  Aaa = U / Ua;
        double  Aep = 1 - Math.exp( -1 * Math.exp( -1 * ((windSpeed * windSpeed) - U) / Aaa) );
        return Aep;
    }
    
    public void calculateCriticalWind( double currentSpacing , double stemWeight, double diameter, double canopyBreadth, double canopyDepth, double [] diam,  double [] Z, double [] mass, double meanDBH  ){
    // Procedure to calculate Critical Wind Speed
        checkEdgeAndGap(); // check Values of Edge and GapWidth
        gustiness  = calculateGustiness(currentSpacing, meanHeight, edge/*93*/ , getGapWidth() ) ;//5);//edge, gapWidth);
        edgeFactor = calculateEdgeFactor(currentSpacing, meanHeight, edge/*edge/*93*/ , getGapWidth() ) ;//);// edge, gapWidth);
      // Calculate GustFactor
        gustFactor = gustiness * edgeFactor;
        
      // Calculate Maximum Overturning Moment as a function of Species, Soil, Cultivation, Drainage and Stem Weight. }
        maxOverturningMoment = calculateMaxOverturningMoment(stemWeight);;
      // calculate Overturning Moment as a ratio between Maximum Overturning Moment and Gust Factor
        overturningMoment = maxOverturningMoment / gustFactor;
      // Calculate Maximum Breaking Moment
        maxBreakingMoment = calculateBreakingBM( diameter );
      // Calculate Breaking Moment as a ratio between Maximum Breaking Moment and the Gust Factor
        breakingMoment = maxBreakingMoment / gustFactor;
      // Calculate MOE
        MOE = calculateMOE();
      // Calculate Critical Wind Speed for Overturning at tree height
        UH_Overturn = calculateWindProcess(overturningMoment, LIMIT, canopyBreadth, canopyDepth, currentSpacing, diam, Z, mass, meanDBH);
      // Calculate Critical Wind Speed for Breaking at tree height

      //UH_Overturn := CalculateWindProcess(OverturningMoment, LIMIT, MeanHeight, CurrentSpacing,
       //              Species_Type, CanopyDepth, CanopyBdth);
      // Calculate Critical Wind Speed for Breaking at tree height
     // UH_Break := CalculateWindProcess(OverturningMoment, LIMIT, MeanHeight, CurrentSpacing,
      //               Species_Type, CanopyDepth, CanopyBdth);
      // Calculate the wind speed at 10 metres above zero plane displacement
        U_Overturn_10 = elevate(UH_Overturn, z0, D, meanHeight);
      // Calculates wind speed at tree top height at the edge of the forest
        UH_OverturnEdge = edgeSpeed(U_Overturn_10, z0, D, meanHeight);
        UH_Break = calculateWindProcess(breakingMoment, LIMIT, canopyBreadth, canopyDepth, currentSpacing, diam, Z, mass, meanDBH);
      // Calculate the wind speed at 10 metres above zero plane displacement  
        U_Break_10    = elevate(UH_Break, z0, D, meanHeight);
      // Calculates wind speed at tree top height at the edge of the forest
        UH_BreakEdge   = edgeSpeed(U_Break_10, z0, D, meanHeight);


        if( U_Overturn_10 < U_Break_10 )uCritical = U_Overturn_10;
        else uCritical = U_Break_10;
    }
        
    //private members
    private int cultivationScore;
    private int soilScore;
    private int speciesScore;
    
    private String species;
    private String soil;
    private String cultivation;
    private String drainage;
    
    private double dams;
    private double meanHeight;
    
    private double gapWidth;
    private double edge;
    private boolean brownEdge;
    private double HeightOfForce;
    private double gustFactor;
    private double gustiness;
    private double edgeFactor;
    private double maxOverturningMoment;
    private double overturningMoment;
    private double maxBreakingMoment;
    private double MOR;
    private double breakingMoment;
    private double MOE;
    private double UH_Overturn;
    private double UH_Break;
    private double U_Overturn_10;
    private double U_Break_10;
    private double UH_BreakEdge;
    private double UH_OverturnEdge;
    private double[]/*WindProfileArray*/ U_UH = new double[ SECTIONS];
    private double porosity;
    private double D;
    private double z0;
    private double gammaSolved;
    private double bendingMoment;
    private double[] mtotal;
    private boolean noDataFlag;
    private boolean beyondRangeFlag;
    private double probOfBreak;
    private double probOfOverturn;
    private double uCritical;
    
    public double getCritWindSpeedBreak(){ return U_Break_10; }
    public double getCritWindSpeedOverturn(){ return U_Overturn_10; }            
    public double getProbOfBreak(){ return probOfBreak;}
    public double getProbOfOverturn(){ return probOfOverturn;}
    
    private double k_weibull = 1.85;
    private double a_weibull = 0.0;
    //constants
    
    private static int    TREE_HEIGHTS_FROM_EDGE = 9;
    private static int    BIGGAP                 = 10;
    private static double LIMIT                  = 0.01;

    private static final double CR = 0.3; //drag coefficient
    private static final double CS = 0.003;
    private static final double CW = 2;
    private static final double K  = 0.4;
    private static final double RO = 1.2226;
    private static final double G  = 9.81;
    private static final double CALC_HT = 10.0;
    private static final double FIELDZ0 = 0.06;
    
  
}
