# barcode-parcer
A repository that scans lets you scan a barcode and gives you some keywords, like product name and model. Zxing embedded 
libraries are used to scan barcodes, then the results are passed to BarcodeParser, which parses google search results to
obtain the product name and model.

# Code example

There are two options for using the repository: starting the barcode scanner activity right away or managing it yourself 
and just passing the barcode value to BarcodeParser and receiving product name.   
