package com.example.covid19.Activities;

//public class Information_News extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
//
//    private AppBarConfiguration mAppBarConfiguration;
//    private BottomNavigationView myBottomView;
//    private FrameLayout frameLayout;
//    private HomeFragment homeFragment;
//    private HospitalFragment hospitalFragment;
//    private NewsFragment newsFragment;
//    private MapFragment mapFragment;
//    private NotifFragment notifFragment;
//
//
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_bottom_bar);
//
//        myBottomView = findViewById(R.id.nav_bar_view);
//        frameLayout = findViewById(R.id.nav_host_bar);
//
//
//        homeFragment = new HomeFragment();
//        hospitalFragment = new HospitalFragment();
//        newsFragment = new NewsFragment();
//        mapFragment = new MapFragment();
//        notifFragment = new NotifFragment();
//
//
//    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.home, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
//                || super.onSupportNavigateUp();
//    }
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//
//
//
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.nav_home) {
//            myBottomView.setItemBackgroundResource(R.color.startblue);
//            setFragment(homeFragment);
//
//        } else if (id == R.id.navigation_news) {
//            myBottomView.setItemBackgroundResource(R.color.startblue);
//            setFragment(newsFragment);
//
//        } else if (id == R.id.navigation_maps) {
//            myBottomView.setItemBackgroundResource(R.color.startblue);
//            setFragment(mapFragment);
//        } else if (id == R.id.navigation_notifications) {
//            myBottomView.setItemBackgroundResource(R.color.startblue);
//            setFragment(notifFragment);
//        } else if (id == R.id.navigation_hospital) {
//            myBottomView.setItemBackgroundResource(R.color.startblue);
//            setFragment(hospitalFragment);
//        }
//        return true;
//    }
//
//
////        switch (item.getItemId()){
////            case R.id.navigation_home:
////                myBottomView.setItemBackgroundResource(R.color.startblue);
////
////                setFragment(homeFragment);
////                return true;
////            case R.id.navigation_news:
////                myBottomView.setItemBackgroundResource(R.color.startblue);
////                setFragment(newsFragment);
////                return true;
////
////            case R.id.navigation_maps:
////                myBottomView.setItemBackgroundResource(R.color.startblue);
////                setFragment(mapFragment);
////                return true;
////
////            case R.id.navigation_notifications:
////                myBottomView.setItemBackgroundResource(R.color.startblue);
////                setFragment(notifFragment);
////                return true;
////
////            case R.id.navigation_hospital:
////                myBottomView.setItemBackgroundResource(R.color.startblue);
////                setFragment(hospitalFragment);
////                return true;
////
////            default:
////                return false;
//
//        //}
//
//
//
//
//
//
//    private void setFragment(Fragment fragment) {
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.nav_host_bar, fragment,"MY_FRAGMENT").commit();
//
//    }
//}
