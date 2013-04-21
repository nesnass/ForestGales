package uk.gov.forestresearch.forestmodels.gales;


public class FGStandU{

public FGStandU(){}
private String soil;
private double soilScore;
private String cultivation;
private String drainage;
private int age;
private int plantingYear;


public int cultivationToInt(String cultivation){
    return 1;
// function to convert Cultivation types to byte as requited in the ForestGALES calculations
/*begin
  case ACultivation of
    ctNone          : Result := 0;  // None
    ctNotch         : Result := 1;  // Notched Planting
    ctCompletePlough: Result := 2;  // Complete Ploughing
    ctShallowPlough : Result := 3;  // Shallow Ploughing < 45cm
    ctDeepPlough    : Result := 4;  // Deep Ploughing > 45cm
    ctScarify       : Result := 5;  // Scarifying
    ctDiscTrench    : Result := 6;  // Disc Trenching
    ctTurf          : Result := 7;  // Turf Planting
    ctMoling        : Result := 8;  // Moling
    ctAlternate     : Result := 9;  // Alternate single/double
    ctContourPlough : Result := 10; // Contour Ploughing
    ctMound         : Result := 11; // Mound Planting
    else
      begin
        MessageDlg('Cultivation type is not correct'+chr(13)+'Please try another type', mtError,[mbOK],0);
        Result:= 0; //  assign a zero value if the type is not correct
      end;
  end;
  //end case
end;
// end CultivationToInt*/
}


public int drainageToInt (String drainage){
    // function to convert Drainage types to byte as requited in the ForestGALES calculations
    if( drainage.equalsIgnoreCase( "none ")) return 0;
    else if( drainage.equalsIgnoreCase( "average") ) return 1;
    else if( drainage.equalsIgnoreCase("poor")) return 2;
    else if( drainage.equalsIgnoreCase("good")) return 3;
    else return 0;
}

/*function FCSoilToInt (AFCSoil : FCSoil): byte;
begin
  case AFCSoil of
    stNone           : Result := 0;     // None
    stBrownEarth     : Result := 1;     // Brown Earth
    stInterBE_IP     : Result := 2;     // Intergrade (Brown Earth-Ironpan)
    stPodzol         : Result := 3;     // Podzol
    stIronPan        : Result := 4;     // Iron Pan
    stInterBE_Gley   : Result := 5;     // Intergrade (Brown Earth-Gley)
    stPeatyGley      : Result := 6;     // Peaty Gley
    stGley           : Result := 7;     // Gley
    stFlushBasin     : Result := 8;     // Flushed Basin Peat
    stFlushBlanket   : Result := 9;     // Flushed Blanket Peat
    stUnflushSphag   : Result := 10;    // Unflushed Sphagnum Bog
    stUnflushBlanket : Result := 11;    // Unflushed Blanket Bog
    stCalcareous     : Result := 12;    // Calcareous Soil
    stSkeletal       : Result := 13;    // Skeletal Soil
    stErodedBog      : Result := 14;    // Eroded Bog
    stLitoral        : Result := 15;    // Littoral Soil
    else
      begin
        MessageDlg('FC Soil type is not correct'+chr(13)+'Please try another type', mtError,[mbOK],0);
        Result:= 0; //  assign a zero value if the type is not correct
      end;
   end;
   //end case
end;
// end FCSoilToInt*/




    public double calcSoilScore(String soil){
        if( soil.equalsIgnoreCase("Brown Earth")) return 0;
        if( soil.equalsIgnoreCase("Integrade (Brown Earth-Ironpan)")) return 0;
        if( soil.equalsIgnoreCase("Podzol")) return 0;
        if( soil.equalsIgnoreCase("Iron Pan")) return 0;
        if( soil.equalsIgnoreCase("Intergrade (Brown Earth-Gley)")) return 10;
        if( soil.equalsIgnoreCase("Peaty Gley"))  return 10;
        if( soil.equalsIgnoreCase("Gley")) return 10;
        if( soil.equalsIgnoreCase("Flushed Basin Peat")) return 5;
        if( soil.equalsIgnoreCase("Flushed Blanket Peat")) return 5;
        if( soil.equalsIgnoreCase("Unflushed Sphagum Bog")) return 5;
        if( soil.equalsIgnoreCase("Unflushed Blanket Bog")) return 5;
        if( soil.equalsIgnoreCase("Calcareous Soil")) return 0;
        if( soil.equalsIgnoreCase("Skeletal Soil")) return 10;
        if( soil.equalsIgnoreCase("Eroded Bog")) return 5;
        if( soil.equalsIgnoreCase("Littoral Soil")) return 0;
        return 10;
    }

    public String getSoil() {
        return soil;
    }

    public void setSoil(String soil) {
        this.soil = soil;
    }

    public double getSoilScore() {
        return soilScore;
    }

    public void setSoilScore(double soilScore) {
        this.soilScore = soilScore;
    }

    public String getCultivation() {
        return cultivation;
    }

    public void setCultivation(String cultivation) {
        this.cultivation = cultivation;
    }

    public String getDrainage() {
        return drainage;
    }

    public void setDrainage(String drainage) {
        this.drainage = drainage;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getPlantingYear() {
        return plantingYear;
    }

    public void setPlantingYear(int plantingYear) {
        this.plantingYear = plantingYear;
    }
}
