# CBR-Currencies
## Description
<p>An Android app for getting currency rates from Central Bank of Russia website.</p>
<p>minSdkVersion 26 (<b><i>Oreo 8.0</i></b>).</p>
<p>Currency rates are stored in local database using Room.</p>

## Inspiration
I made this app because it was a test task for internship.

## First launch:
<img src="https://github.com/Girrafeec/CBR-Currencies/blob/main/images/first_launch.gif"/>

<p>Users are able to expand currency info by clicking on "drop down" button.</p>
<img src="https://github.com/Girrafeec/CBR-Currencies/blob/main/images/currency_extended.jpg"/>

## Currency convertor:
<p>Users are able to convert currnecy from roubles to any currency and from any currency to roubles.<p>
<p>The process of convertion is based on last currency rate update.</p>
<img src="https://github.com/Girrafeec/CBR-Currencies/blob/main/images/converter.gif"/>

## Rate update:

### User update
<p>Users are able to update currency rates whenever they want.</p>
<img src="https://github.com/Girrafeec/CBR-Currencies/blob/main/images/user_update.gif"/>

### Background update
<p>Central Bank of Russia set currency rates one a day at 11:30 am, so app performs background currency rate update every day at 12:00 pm.</p>
<img src="https://github.com/Girrafeec/CBR-Currencies/blob/main/images/background_update.gif"/>
