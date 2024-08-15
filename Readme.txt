This project implements some of the basic and advanced features of image processing

Some basic functions include image rotating, resizing etc.
Advanced functions include applying color effects, Optical Character Recognition (OCR) etc.

This is a 100% backend application which is use-able by hosting on AWS Lambda.

**** TESTING ****
**IMPORTANT NOTE**
Out of all the functions you can choose to do ONLY 1 at a time. This bar is added to avoid unwanted results in any way.
If you attempt to use multiple functions together, an exception will be thrown.

1. JAR package in target directory must be uploaded to AWS Lambda

2. In Runtime Settings, configure Handler to "org.connector.ImageHandler::handleRequest" without quotes.

3. To test on AWS Lambda, store images in S3 bucket and specify the source and target buckets.

4. Use JSON Format given in testing.json to test the application