# Data Compression

This repository includes two projects on image compression and was developed for the course Data Compression at National Taiwan Normal University in Taipei, fall of 2014. 

The two projects were implemented in Java and handle encoding and decoding of grayscale, 256 bit, image files. Although being in the same repository, the lossless and lossy projects are independent. The results are based on running the codec on the five RAW images given in the testdata folder.


<!-- MarkdownTOC -->

- Author
- Installation
- Lossless Image Compression
- Lossy Image Compression
- References

<!-- /MarkdownTOC -->



## Author
Marcus Windmark, http://windmark.se




### Compiling

Either import the individual projects with an IDE of your choice or manually compile the java files in the src folder. 



## Lossless Image Compression

Adaptive Huffman Coding was used to implement the Lossless Codec. It was implemented by sequentially inserting into a binary tree and balancing according to the algorithm of [Adaptive Huffman Code](#references).


### Executing

After compiling, the following commands can be run.

Encoding: java AdaptiveHuffmanEncode input_file output_file output_huffman_table_file
Decoding: java AdaptiveHuffmanDecode input_file output_file


### Results

| File  | Raw Size (b) | Encoded Size (b) | Space Saving  |
|:------|:------------:|:----------------:|:-------------:|
| test1 | 1920000      | 1802687 		  | 6.11%         | 
| test2 | 1920000      | 1272218 		  | 33.74%        |
| test3 | 1920000      | 1566242 		  | 18.42%        |
| test4 | 1920000      | 1488520 		  | 22.47%        |
| test5 | 1920000      | 1921055 		  | -0.05%        |

Average encoding speed of 400 ms, decoding speed of 300 ms.


## Lossy Image Compression

The implementation of the Lossy Codec uses Discrete Cosine Transform (DCT) and Scalar Quantization together with the same Adaptive Huffman Coding as the lossless codec. Togethery, they form an algorithm of Basic JPEG.

### Executing
After compiling, the following commands can be run.

Encoding: java TransformImageEncode raw_file encoded_file
Decoding: java TransformImageDecode encoded_file decoded_file
SNR: java SignalToNoiseRatio original_file decoded_file

### Results

| File  | Raw Size (b) | Encoded Size (b) | Space Saving | Compression Ratio | SNR   | Encoding Time (ms) | Decoding Time (ms) |
|:------|:------------:|:----------------:|:------------:|:-----------------:|:-----:|:------------------:|:------------------:|
| test1 | 1920000      | 383654  		  | 80.02%       | 5.00 			 | 26.84 | 836 				  | 825 			   |
| test2 | 1920000      | 266754  		  | 86.11%       | 7.20 			 | 17.73 | 848 				  | 780 			   |
| test3 | 1920000      | 317213  		  | 83.48%       | 6.05 			 | 27.88 | 785 				  | 777 			   |
| test4 | 1920000      | 310056  		  | 83.85%       | 6.19 			 | 26.27 | 807 				  | 773 			   |
| test5 | 1920000      | 867319  		  | 54.83%       | 2.21 			 | 18.00 | 973 				  | 859 			   |


## References
1. Donald E Knuth, Dynamic huffman coding, Journal of Algorithms, Volume 6, Issue 2, June 1985, Pages 163-180, ISSN 0196-6774, http://dx.doi.org/10.1016/0196-6774(85)90036-7.









