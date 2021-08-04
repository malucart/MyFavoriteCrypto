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
* [ ] Profile page

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

- (DATABASE) Local and external database
 ```swift
    // RequestQueue -> all the requests are queued up that has to be executed
     RequestQueue queue = Volley.newRequestQueue(context);
     // making a json object request to fetch data from API
     JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, API_URL, null, new Response.Listener<JSONObject>() {
         @Override
         public void onResponse(JSONObject response) {
             // extracting data from response and passing it to array list
             // bar visibility to gone
             pbLoading.setVisibility(View.GONE);
             try {
                 // extracting data from json
                 JSONArray dataArray = response.getJSONArray("data");
                 for (int i = 0; i < dataArray.length(); i++) {
                     JSONObject dataObject = dataArray.getJSONObject(i);
                     String name = dataObject.getString("name");
                     String id = dataObject.getString("id");
                     String symbol = dataObject.getString("symbol");
                     // we need to get quote and usd because price is inside of an array of the json array
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
                 // handling json exception
                 e.printStackTrace();
                 Toast.makeText(context, getString(R.string.missing), Toast.LENGTH_SHORT).show();
             }
         }
     }, error -> {
         // displaying error response when received any error while json object request to fetch data from API!!
         Toast.makeText(context, getString(R.string.missing), Toast.LENGTH_SHORT).show();
     }) {
         @Override
         // map is interface and hashmap is a class that implements map
         public Map<String, String> getHeaders() {
             // passing headers as key along with API keys
             // we want to associate a key with a value so hashmap is the best option
             HashMap<String, String> headers = new HashMap<>();
             headers.put(context.getString(R.string.pro_api_key), context.getString(R.string.coin_api_key));
             return headers;
         }
     };
     // add all the json object data we request from the API to the queue
     queue.add(jsonObjectRequest);
 ```

- (DATABASE) Local and external database
 ```swift
    public void insertIntoDatabase(CryptoModel model) {
        ArrayList<CryptoModel> favList = favDB.getFavListFromDatabase();

        // Let's ensure that we don't have it already in the favorite list
        for (int i = 0 ; i < favList.size(); i++) {
            // if it's already in the list, just return and don't do anything
            if (favList.get(i).getSymbol().equals(model.getSymbol())) {
                return;
            }
        }

        // update local and external database
        favDB.insertDataIntoDatabase(model);
        setIsRemoteUpdateDatabase(true);
    }
 ```

 ```swift
    public void deleteCryptoModelFromRemoteDatabase(String objectId) {
            ParseQuery<com.parse.ParseObject> query = ParseQuery.getQuery("FavoriteModel");

            // Retrieve the object by id
            query.getInBackground(objectId, (object, e) -> {
                if (e == null) {
                    //Object was fetched
                    //Deletes the fetched ParseObject from the database
                    object.deleteInBackground(e2 -> {
                    });
                }
            });
        }
 ```

 ```swift
    public void deleteCryptoModelFromRemoteDatabaseSlowPath(CryptoModel model) {
        // This is the slowest case, when there is a "de-sync" between the remote and
        // local database and we don't know the object id, so we need to pull the
        // remote database and find the target object we want to remove, this is slow
        // as we end up doing two request (one to download and another for deleting)

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("FavoriteModel");

        query.findInBackground(new com.parse.FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                for(ParseObject p : list) {

                    // same symbol, is our target
                    if (p.get("symbol").equals(model.getSymbol())) {
                        deleteCryptoModelFromRemoteDatabase(p.getObjectId());
                        return;
                    }
                }
            }
        });
    }
 ```
### Animation
- (LOGIN SCREEN) Type Writer Effect
 ```swift
    // when you create a new Handler, it is bound to the thread/message queue of the thread that is creating it
    private Handler mHandler = new Handler();

    // used to type each letter of the text to display at some defined delay by calling the postDelayed method of the Handler object
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
        mHandler.removeCallbacks(characterAdder); // removeCallbacks simply removes those runnables who have not yet begun processing from the queue
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
