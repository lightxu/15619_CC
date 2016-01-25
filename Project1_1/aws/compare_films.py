import sys

filename = sys.argv[1]

views_2014 = 0
views_2015 = 0
with open(filename, "r") as fin:
	for line in fin:
		items = line.split()
		if "2014_film" in items[0]:
			views_2014 = views_2014 + int(items[1])
		elif "2015_film" in items[0]:
			views_2015 = views_2015 + int(items[1])
# print views_2014
# print views_2015
if (views_2014 > views_2015):
	print "2014"
else:
	print "2015"
