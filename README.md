MyFavoriteCrypto - README
===

# MyFavoriteCrypto

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
Simple app that tracks cryptocurrency. It helps you to think twice before buying a crypto, or it helps you to buy one as soon as you can. You can see the data from 1h ago, 24h ago, 1 week ago, and the total value of the crypto. Everything in real time!

### App Evaluation
[Evaluation of your app across the following attributes]
- **Category:** Tracking App
- **Mobile:** Mobile is a good option for this kind of app because of the mobility and easy layout.
- **Story:** Some people like to think twice before buying a new cryptocurrency, so when they read the news, they see the specific crypto’s situation in the last days, months and the last year. And after monitoring and studying the crypto, they may buy it. So the idea of this app is to track crypto, and have a page for the favorite crypto by the user.
- **Market:** Everyone who is interested in investments. 
- **Habit:** People will be constantly using this app to track their crypto, to see new crypto and to make their life easier to not search about a specific crypto outside because the app is already giving them the information they need.
- **Scope:** V1 would allow users to create their profile. V2 would be able to connect to the CoinMarketCap API. V3 would be able to save favorite items (crypto) V4 would be able to delete an item (crypto) on their favorite list.
https://coinmarketcap.com/api/documentation/v1/# (API)
https://www.infoworld.com/article/3226733/graphlib-an-open-source-android-library-for-graphs.html (External library to plot graphics)

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* The user can sign in/up with their Facebook account
* The user can see a list of crypto data (Recycler View)
* The user can like how many crypto they want
* User's liked page are saved between sesions
* The user can delete how many crypto they want on their liked page
* In the user's liked page, it's possible to plot a graphic to see better the data of their favorite crypto

**Optional Nice-to-have Stories**

* Reddit info page
* Profile page
* Discursion page, so people who are signing in the app can talk with each other about crypto

### 2. Screen Archetypes

* Splash Screen with a brand
* Login Screen
   * User can log in
   * User can sign up
* Sign up Screen
   * Redirects the user to Facebook's registration
* Home
    * Allow the user to add their favorite crypto to a list (liked page)
* Liked Page Screen
    * Their favorite crypto info with a graphic

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
<img src="https://github.com/" width=600>

### [BONUS] Interactive Prototype

## Schema 
[This section will be completed in Unit 9]
### Models
[Add table of models]
### Networking
- [Add list of network requests by screen ]
- [Create basic snippets for each Parse network request]
- [OPTIONAL: List endpoints if using existing API such as Yelp]
