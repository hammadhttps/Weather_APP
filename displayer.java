class displayer implements  display{
    private pollution_data pd1=new pollution_data();
    private dialogue d1=new dialogue();
    private sunrise_sunset ss1=new sunrise_sunset();
    private weatherlogic wl1=new weatherlogic();

    @Override
    public void display_data(double lat, double lon, String name,int num) {
        if(num==1){
            pd1.get_pollutiondata(lat,lon);
        } else if(num==2){
            d1.get_basic_dialogues(name);
        } else if(num==3){
            wl1.check_weather_with_name(name);
        } else if(num==4){
            wl1.check_weather_with_lat_lon(lat,lon);
        }
    }

    @Override
    public void show_weather_conditions(String name) {
        d1.get_basic_dialogues(name);
        ss1.get_sunrisesunset(name);
        wl1.check_weather_with_name(name);
    }
}