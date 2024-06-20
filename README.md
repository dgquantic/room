# Room Occupancy Manager

This is a tool designed to assist hotels in optimizing their room occupancy. The tool takes into account the number of available Premium and Economy rooms for a particular night and the potential guests' willingness to pay.

The hotel has two room categories:

1. **Premium**
2. **Economy**

The policy is such that customers willing to pay EUR 100 or more are not allocated an Economy room. However, if the Premium rooms are vacant and all Economy rooms are occupied, lower-paying customers could be upgraded to Premium rooms. In such a situation, customers under EUR 100 who are willing to pay the highest amount get preference for the upgrade.

The application provides an API that:

- Allows hotels to input the number of available rooms across Premium and Economy categories.
- Calculates and displays the number bathrooms that will be occupied in each category and the total revenue.

For the purpose of tests, a JSON file that reflects potential guests' willingness to pay will be used as mock data. For instance `[23, 45, 155, 374, 22, 99.99, 100, 101, 115, 209]`.
Please see all the test case located in the resources' directory .

## Functionality

### 1. Enter Available Rooms
Hotels can enter the number of available rooms for both Premium and Economy categories.

### 2. Compute Occupancy and Income
The tool calculates and displays how many rooms of each category will be occupied and the total income.

### 3. Guests Representation
Potential guests are represented in an array that comprises numbers indicating their willingness to pay for the night.

By utilizing this tool, hotels can both maximize their revenue and improve upon their room allocation strategies.
## Prerequisites

Make sure you have the following installed:

- Java SDK 21
- Maven



## How to Run the Application

1. Clone the repository:https://github.com/dgquantic/room
2. Build the project using maven : mvn package
3. Running the test :mvn test



