If running from Uni with proxy, 
1. Open Driver.java
2. Remove the comment symbol from line 90, 91 and 92
3. Run the program as below

To run the system from a terminal, 
1. Go to the source folder
2. Compile the files by typing "javac *.java"
3. Run the file by typing "java Driver"
4. Wait for the historical data of 30 stocks and the market index (ASX200) to be downloaded
5. Once the historical data have successfully been downloaded, the option box is displayed 
6. Enter option below the option box
7. Once the operation has successfully finished, the option box will be displayed again.

The following is an explanation of the options:

**************************************************************************************************************************************

"0 - Exit"
This causes the program to terminate.

"1 - Select stock" 
Select a stock from the list provided to run static strategies. 

ASSIGNMENT PART 1 - RANDOM
"3 - Random"
Generates a random number that determines which transaction (buy / sell) to perform.

"4 - ROI"
This calculates the return on investment for the current stock and the market index.

"5 - Volatility"
This calculates the volatility for the current stock and the market index.

ASSIGNMENT PART 2 - STRATEGIES
"6 - Moving Average"
Runs moving average on the current stock. A report of the results is generated.

"7 - Price Momentum"
Runs price momentum on the current stock. A report of the results is generated.

"8 - Rate of Change"
Runs rate of change on the current stock. A report of the results is generated.

"9 - Moving Average Convergence-Divergence"
Runs moving average convergence-divergence on the current stock. A report of the results is generated.

"10 - Relative Strength Index"
Runs relative strength index on the current stock. A report of the results is generated.

ASSIGNMENT PART 3 - ADAPTIVE STRATEGIES
"11 - Adaptive Moving Average" 
This applies the adaptive moving average strategy to 30 stocks. A report for each stock is generated.

"12 - Adaptive Price Momentum"
This applies the adaptive price momentum strategy to 30 stocks. A report for each stock is generated.

"13 - Adaptive Rate Of Change"
This applies the adaptive rate of change strategy to 30 stocks. A report for each stock is generated.

"14 - Adaptive Moving Average Convergence-Divergence" (FIX THE HYPHEN)
This applies the adaptive moving average convergence-divergence strategy to 30 stocks. A report for each stock is generated.

"15 - Adaptive Relative Strength Index"
This applies the adaptive relative strength index strategy to 30 stocks. A report for each stock is generated.

"16 - Combination Adaptive"
This applies a combination of the 5 adaptive strategies to 30 stocks. A report for each stock is generated.

EDITING TOOLS
"20 - Set Transaction Cost"
This changes the transaction cost.

"21 - Get Transaction Cost"
This displays the current transaction cost.

"22 - Edit window for adaptive strategies"
This is used for changing the window update period and the period between updates.

Autorun
"30 - Autorun Part 1"
This automatically applies random strategies to the first 10 stocks in the stocks list. A report for each stock is generated.

"31 - Autorun Part 2"
This automatically applies all 5 static strategies to the first 10 stocks in the stocks list. A report for each stock is generated.

**************************************************************************************************************************************

How to achieve the results as included in the report:
To run the program for part 1, select option 30

To run the program for part 2 (non-automated), select option 1 to select desired stock, then select a strategy to use from the list of strategies.

To run the program for part 2 (automated), select option 31

To run the program for part 3, select options 11, 12, 13, 14, 15, or 16. Each selection automatically simulates all stocks. 

To change the transaction cost or window size, select from options 20 or 22. 

**Note:
1. Transaction cost is set to 1.5% by default.
2. Window update size is set to 100 working days and the period before the next update is set to 20 working days by default.
