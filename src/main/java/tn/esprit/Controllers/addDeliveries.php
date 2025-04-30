<?php
// Connect to the database
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "tuni'art";

$conn = new mysqli($servername, $username, $password, $dbname);

// Check connection
if ($conn->connect_error) {
  die("Connection failed: " . $conn->connect_error);
}

// Get the agency name from the POST data
$agencyName = $_POST['agencyName'];

// Add the delivery to the table
$sql = "INSERT INTO deliveries (agency_name) VALUES ('$agencyName')";
if ($conn->query($sql) === TRUE) {
  // Increase the deliveries number for the selected agency
  $sql = "UPDATE delivery_agency SET nb_deliveries = nb_deliveries + 1 WHERE agency_name = '$agencyName'";
  if ($conn->query($sql) === TRUE) {
    echo 'Delivery added successfully';
  } else {
    echo 'Error adding delivery: ' . $conn->error;
  }
} else {
  echo 'Error adding delivery: ' . $conn->error;
}

// Close the connection
$conn->close();
