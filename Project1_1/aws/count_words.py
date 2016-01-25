import sys

filename = sys.argv[1]
word_count = 0
with open(filename, "r") as fin:
	for line in fin:
		title = line.lower().split()[0]
		if "cloud" in title and "computing" in title:
			# print title
			word_count = word_count + 1
print word_count
