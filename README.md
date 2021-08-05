MyFavoriteCrypto
===

## Table of Contents
1. [Overview](#Overview)
2. [Product Spec](#Product-Spec)
3. [Wireframes](#Wireframes)
4. [Models](#Models)
5. [Networking](#Network)
6. [Animation](#Animation)
7. [API](#API)

## Overview
### Description
Simple app that tracks cryptocurrency. It helps you to think twice before buying a crypto, or it helps you to buy one as soon as you can. You can see the data from 1h ago, 24h ago, 1 week ago, and the total value of the crypto. Everything in real time!

### App Evaluation
[Evaluation of your app across the following attributes]
- **Category:** Tracking App
- **Mobile:** Mobile is a good option for this kind of app because of the mobility and easy layout.
- **Story:** Some people like to think twice before buying a new cryptocurrency, so when they read the news, they see the specific crypto’s situation in the last days, months and the last year. And after monitoring and studying the crypto, they may buy it. So the idea of this app is to track crypto, and have a page for the FavoriteModel crypto by the user.
- **Market:** Everyone who is interested in investments. 
- **Habit:** People will be constantly using this app to track their crypto, to see new crypto and to make their life easier to not search about a specific crypto outside because the app is already giving them the information they need.
- **Scope:** V1 would allow users to create their profile. V2 would be able to connect to the CoinMarketCap API. V3 would be able to save FavoriteModel items (crypto) V4 would be able to delete an item (crypto) on their FavoriteModel list.
- https://coinmarketcap.com/api/documentation/v1/# (API)
- https://github.com/jjoe64/GraphView (External library to plot graphics)

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* [x] The user can sign in/up with their Facebook account
* [x] The user can see a list of crypto data (Recycler View) showing crypto's name, symbol, current price, percentage of price changed in the last 1h, 24h and 7d
* [x] The user can like how many crypto they want
* [x] User's liked page are saved between sessions (locally and externally database)
* [x] The user can delete how many crypto they want on their liked page
* [x] The user can see a plotted graph of their favorite crypto data (Time(h) vs Price($))

**Optional Nice-to-have Stories**

* [x] Reddit button that goes to Reddit web page
* [x] Graph plotted has the possibility to zoom
* [x] Shimmer effect

### 2. Screen Archetypes

* Splash Screen with a brand
* Login Screen
   * User can log in
   * User can sign up
* Sign up Screen
   * Redirects the user to Facebook's registration
* Home
    * Allow the user to add their FavoriteModel crypto to a list (liked page)
* Liked Page Screen
    * Their FavoriteModel crypto info with a graph

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Sign in/up
* Home
* Liked Page

**Flow Navigation** (Screen to Screen)

* Login Screen
* Crypto data List
* Liked Page that plots graphics

## Digital Wireframes
<img src="https://github.com/malucart/MyFavoriteCrypto/blob/main/wireframes.png" width=600>

### [BONUS] Interactive Prototype
<img src="https://github.com/malucart/MyFavoriteCrypto/blob/main/cryptotracker.gif">

### Models
Crypto
|    Property    |   Type   |          Description           |
| -------------- | -------- | ------------------------------ |
| name           | String   | crypto's name                  |
| symbol         | String   | crypto's symbol                |
| logoURL        | String   | crypto's logo                  |
| price          | Double   | crypto's current price         |
| oneHour        | Double   | crypto's price changed 1h ago  |
| twentyFourHour | Double   | crypto's price changed 24h ago |
| oneWeek        | Double   | crypto's price changed 7d ago  |
| favStatus      | Boolean  | empty or fully heart           |

Favorite
|      Property      |    Type   |          Description           |
| ------------------ | --------- | ------------------------------ |
| KEY_USER           | ParseUser | current user logged            |
| KEY_NAME           | String    | crypto's name                  |
| KEY_SYMBOL         | String    | crypto's symbol                |
| KEY_LOGOURL        | Double    | crypto's logo                  |
| KEY_PRICE          | Double    | crypto's current price         |
| KEY_ONEHOUR        | Double    | crypto's price changed 1h ago  |
| KEY_TWENTYFOURHOUR | Double    | crypto's price changed 24h ago |
| KEY_ONEWEEK        | Double    | crypto's price changed 7d ago  |
| KEY_FAVSTATUS      | Boolean   | empty or fully heart           |

### Network
- (LOGIN) Login user with Facebook
 ```swift
    btnLogin.setOnClickListener(v -> {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle(getString(R.string.title));
        dialog.setMessage(getString(R.string.message));
        dialog.show();
        Collection<String> permissions = Arrays.asList(getString(R.string.publicProfile), getString(R.string.email));
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .enableLocalDataStore()
                .build());
        ParseFacebookUtils.logInWithReadPermissionsInBackground(this, permissions, (user, callback) -> {
            dialog.dismiss();
            if (callback != null) {
                Log.e(TAG_FB, getString(R.string.done), callback);
                Toast.makeText(this, callback.getMessage(), Toast.LENGTH_LONG).show();
            } else if (user == null) {
                Toast.makeText(this, getString(R.string.loginCancelled), Toast.LENGTH_LONG).show();
                Log.d(TAG_FB, getString(R.string.loginCancelled));
            } else if (user.isNew()) {
                Toast.makeText(this, getString(R.string.signedUp), Toast.LENGTH_LONG).show();
                Log.d(TAG_FB, getString(R.string.signedUp));
                getUserDetailFromFB();
            } else {
                Toast.makeText(this, getString(R.string.loggedIn), Toast.LENGTH_LONG).show();
                Log.d(TAG_FB, getString(R.string.loggedIn));
                getUserDetailFromParse();
            }
        });
    });
 ```

- (API) CoinMarketCap API request
 ```swift
     RequestQueue queue = Volley.newRequestQueue(context);
     JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, API_URL, null, new Response.Listener<JSONObject>() {
         @Override
         public void onResponse(JSONObject response) {
             pbLoading.setVisibility(View.GONE);
             try {
                 JSONArray dataArray = response.getJSONArray("data");
                 for (int i = 0; i < dataArray.length(); i++) {
                     JSONObject dataObject = dataArray.getJSONObject(i);
                     String name = dataObject.getString("name");
                     String id = dataObject.getString("id");
                     String symbol = dataObject.getString("symbol");
                     JSONObject quote = dataObject.getJSONObject("quote");
                     JSONObject usd = quote.getJSONObject("USD");
                     double price = usd.getDouble("price");
                     double oneHour = usd.getDouble("percent_change_1h");
                     double twentyFourHour = usd.getDouble("percent_change_24h");
                     double oneWeek = usd.getDouble("percent_change_7d");

                     String logo = "https://s2.coinmarketcap.com/static/img/coins/128x128/" + id + ".png";

                     cryptoModels.add(new CryptoModel(name, symbol, logo, price, oneHour, twentyFourHour, oneWeek));
                 }

             } catch (JSONException e) {
                 e.printStackTrace();
                 Toast.makeText(context, getString(R.string.missing), Toast.LENGTH_SHORT).show();
             }
         }
     }, error -> {
         Toast.makeText(context, getString(R.string.missing), Toast.LENGTH_SHORT).show();
     }) {
         @Override
         public Map<String, String> getHeaders() {
             HashMap<String, String> headers = new HashMap<>();
             headers.put(context.getString(R.string.pro_api_key), context.getString(R.string.coin_api_key));
             return headers;
         }
     };
     queue.add(jsonObjectRequest);
 ```

- (DATABASE) External database
 ```swift
    public void insertIntoDatabase(CryptoModel model) {
        ArrayList<CryptoModel> favList = favDB.getFavListFromDatabase();

        for (int i = 0 ; i < favList.size(); i++) {
            if (favList.get(i).getSymbol().equals(model.getSymbol())) {
                return;
            }
        }

        favDB.insertDataIntoDatabase(model);
        setIsRemoteUpdateDatabase(true);
    }
 ```

 ```swift
    public void deleteCryptoModelFromRemoteDatabase(String objectId) {
            ParseQuery<com.parse.ParseObject> query = ParseQuery.getQuery("FavoriteModel");

            query.getInBackground(objectId, (object, e) -> {
                if (e == null) {
                    object.deleteInBackground(e2 -> {
                    });
                }
            });
        }
 ```

 ```swift
    public void deleteCryptoModelFromRemoteDatabaseSlowPath(CryptoModel model) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("FavoriteModel");

        query.findInBackground(new com.parse.FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                for(ParseObject p : list) {

                    if (p.get("symbol").equals(model.getSymbol())) {
                        deleteCryptoModelFromRemoteDatabase(p.getObjectId());
                        return;
                    }
                }
            }
        });
    }
 ```

 - (DATABASE) SQLite database
  ```swift
// insert data into database
    public void insertDataIntoDatabase(@NonNull CryptoModel model) {
        // writes data in the database
        SQLiteDatabase db = this.getWritableDatabase();
        // creates a variable for content values
        ContentValues cv = new ContentValues();
        // passing all values along with its key and value pair
        cv.put(NAME, model.getName());
        cv.put(SYMBOL, model.getSymbol());
        cv.put(LOGO_URL, model.getLogoURL());
        cv.put(PRICE, model.getPrice().toString());
        cv.put(ONE_HOUR, model.getOneHour().toString());
        cv.put(TWENTY_FOUR_HOUR, model.getTwentyFourHour().toString());
        cv.put(ONE_WEEK, model.getOneWeek().toString());
        cv.put(OBJECT_ID, model.getObjectId());
        // after adding all values it passes content values to the table
        db.insert(FAVORITE_TABLE,null, cv);
        // closing database after adding into the database
        db.close();
    }
  ```

  ```swift
  // read data from the database
      public ArrayList<CryptoModel> getFavListFromDatabase() {
          // database for reading our database.
          SQLiteDatabase db = this.getWritableDatabase();
          // creates a cursor with query to read data from the database
          // rawQuery reads queries
          Cursor cursor = db.rawQuery("select * from " + FAVORITE_TABLE, null);
          // creates a new array list
          ArrayList<CryptoModel> favList = new ArrayList<>();
          // moving the cursor to first position
          if (cursor.moveToFirst()) {
              do {
                  CryptoModel aux = new CryptoModel(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getDouble(4), cursor.getDouble(5), cursor.getDouble(6), cursor.getDouble(7));
                  aux.setObjectId(cursor.getString(8));
                  // adds the data from cursor to the array list created
                  favList.add(aux);
              } while (cursor.moveToNext());
              // moving the cursor to next
          }

          // closes the cursor and returns the array list
          cursor.close();
          db.close();
          return favList;
      }
  ```

  ```swift
  // remove from database
      public void removeFavorite(String symbol) {
          SQLiteDatabase db = this.getWritableDatabase();
          db.delete(FAVORITE_TABLE, "SYMBOL = ?", new String[] {symbol});
          db.close();
      }
  ```
### Animation
- (LOGIN SCREEN) Type Writer Effect
 ```swift
    private Handler mHandler = new Handler();

    private Runnable characterAdder = new Runnable() {
        @Override
        public void run() {
            setText(mText.subSequence(0, mIndex++));
            if (mIndex <= mText.length()) {
                mHandler.postDelayed(characterAdder, mDelay);
            }
        }
    };

    public void animateText(CharSequence txt) {
        mText = txt;
        mIndex = 0;

        setText("");
        mHandler.removeCallbacks(characterAdder);
        mHandler.postDelayed(characterAdder, mDelay);
    }

    public void setCharacterDelay(long m) {
        mDelay = m;
    }
 ```
### API
- (API) CoinMarketCap API
- Base Url: [https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest]()
- Cache/Update frequency: Every 60 seconds
- Endpoint: default 100
- Network: HTTP library called Volley
