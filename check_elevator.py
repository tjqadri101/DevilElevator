import os.path,sys 

def logerror(line, ln, msg):
	print ""
	print "Error: " + msg
	if inFile=="Input":
		print "Line " + str(ln) + " in " + input_path + ": " + line + "\n"
	else:
		print "Line " + str(ln) + " in " + output_path + ": " + line + "\n"
	sys.exit()
	return

def get_currfloor(rider):
	return riders[rider][len(riders[rider])-1][1]

def rider_check_floor_connected(line, ln, rider, startfloor):
	currfloor = get_currfloor(rider)
	if startfloor != currfloor:
		logerror(line, ln, ": Rider " + str(rider) + " was at floor " + str(currfloor) + " and currently start from floor " + str(startfloor))
	return

def check_input_format(line,ln):
	array_line = line.strip().split(" ")
	if ln==1:
		if len(array_line)!=4:
			logerror(line, ln, "Input file in incorrect format! First line should contain four numbers F E R N")
	else:
		if len(array_line)!=3:
			logerror(line, ln, "Input file in incorrect format! Each line other than first line should contain three numbers: rider number, starting floor, and destination floor")
	check_digit_zero(line,ln)
	return

def check_input_bound(line,ln, rider,startfloor,destfloor):
	if rider > num_rider:
		logerror(line, ln, "Rider ID exceed rider number!")
	if startfloor > num_floor:
		logerror(line, ln, "Starting floor exceed number of floor")
	if destfloor > num_floor:
		logerror(line, ln, "Destination floor exceed number of floor")
	return
		

def check_digit_zero(line,ln):
	for num in line.strip().split(" "):
		if not num.isdigit():
			logerror(line, ln, "Input file should contain only numbers!")
			return
		if int(num)==0:
			logerror(line, ln, "All integer in input file should start from 1!")
			return
	return

def parse_input():
	global num_rider
	global num_floor
	global num_ele
	global capacity
	ln = 0
	line = input_file.readline().strip().replace("\n","")
	ln += 1
	while line:
		while line=="":
			line = input_file.readline().strip().replace("\n","")
			ln += 1
			continue
		if ln==1:
			check_input_format(line, ln)
			array_line = line.strip().split(" ")
			num_floor = int(array_line[0])
			num_ele = int(array_line[1])
			num_rider = int(array_line[2])
			capacity = int(array_line[3])
		else:
			check_input_format(line, ln)
			array_line = line.strip().split(" ")
			rider = int(array_line[0])
			startfloor = int(array_line[1])
			destfloor = int(array_line[2])
			check_input_bound(line, ln, rider, startfloor, destfloor)
			if rider not in riders.keys():
				riders[rider] = [[startfloor,destfloor, -1]]
			else:
				rider_check_floor_connected(line, ln, rider,startfloor)
				riders[rider].append([startfloor,destfloor, -1])
		line = input_file.readline().strip().replace("\n","")
		ln += 1
	
	if len(riders.keys())>num_rider:
		print "Error in Input file: Too many riders!"
		sys.exit()

	print "Input file valid!"
	return

def check_rider_exist(line,ln,rider):
	if rider not in riders.keys():
		logerror(line, ln, "Rider " + str(rider) + " is not listed in input file")
	if len(riders[rider]) == 0:
		logerror(line, ln, "Rider " + str(rider) + " has finished all requests")
	return

def rider_check_direction(line,ln,rider,direction):
	startfloor = riders[rider][0][0]
	destfloor = riders[rider][0][1]
	if (destfloor>startfloor and direction=='D'):
		logerror(line,ln,"Rider " +str(rider)+ " pushes wrong direction!")
	if (destfloor<startfloor and direction=='U'):
		logerror(line,ln,"Rider " +str(rider)+ " pushes wrong direction!")
	return

def check_in_elevator(line,ln,rider,elevator):
	inElevator = riders[rider][0][2] 
	if inElevator < 0:
		logerror(line, ln, "Rider" + str(rider) + "is not in elevator " + str(elevator))
	if inElevator != elevator:
		logerror(line, ln, "Rider" + str(rider) + "is in elevator " + str(elevator) + " but try to take action on elevator " + str(elevator))
	return

def check_in_building(line,ln,rider):
	inElevator = riders[rider][0][2] 
	if inElevator > 0:
		logerror(line, ln, "Rider " + str(rider) + " is already in elevator " + str(inElevator))
	return
	
def check_output_format(line, ln):
	if len(line.strip().split(" "))==0:
		logerror(line, ln, "Output format incorrect!")
	firstChar = line.strip().split(" ")[0][0]
	if firstChar=='R':
		check_output_rider_format(line,ln)
	elif firstChar == 'E':
		check_output_elevator_format(line,ln)
	else:
		logerror(line, ln, "Output format incorrect!")
	return

def output_check_digit(line, ln, num):
	if not num.isdigit():
		logerror(line, ln, "Output format incorrect! expect digit but observe: " + num)
	if int(num) == 0:
		logerror(line, ln, "Output format incorrect! Integer starts from 1")

def check_output_rider_format(line, ln):
	line_array = line.strip().split(" ")
	Rrider = line_array[0]
	if len(Rrider) < 2:
		logerror(line, ln, "Rider ID exceed number of riders")
	output_check_digit(line, ln, Rrider[1:])
	if int(Rrider[1:]) > num_rider:
		logerror(line, ln, "Rider ID exceed number of riders")
	if len(line_array) < 3:
		logerror(line, ln, "Output format incorrect!")
	action = line_array[1]
	if action == "pushes":
		if len(line_array) != 3:
			logerror(line,ln,"Output format incorrect!")
		if line_array[2][0]=='U' or line_array[2][0]=='D':
			output_check_digit(line, ln, line_array[2][1:])
		elif line_array[2][0]=='E':
			if 'B' not in line_array[2]:
				logerror(line, ln, "Output format incorrect!")
			output_check_digit(line, ln, line_array[2][1:].split('B')[0])
			output_check_digit(line, ln, line_array[2][1:].split('B')[1])
		else:
			logerror(line,ln,"Unknown Syntax : " + line_array[2][0])
	elif action == "enters":
		if len(line_array) != 5:
			logerror(line,ln,"output format incorrect!")
		output_check_digit(line, ln, line_array[2][1:])
		output_check_digit(line, ln, line_array[4][1:])
	elif action == "exits":
		if len(line_array) != 5:
			logerror(line,ln,"Output format incorrect!")
		output_check_digit(line, ln, line_array[2][1:])
		output_check_digit(line, ln, line_array[4][1:])
	else:
		logerror(line, ln, "Action " + str(action) + " is not valid for rider!")
	
def check_capacity(line, ln, elevator, action):
	if elevators[elevator][2]>=capacity:
		logerror(line, ln, "Elevator " + str(elevator) + " is full")
	return

def rider_check_floor(line, ln, rider, floor, elevator, action):
	if action=="enters" and floor!=riders[rider][0][0]:
		logerror(line, ln, "Rider " + str(rider) + " is not at floor " + str(floor))
	if action=="exits" and floor!=riders[rider][0][1]:
		print floor
		logerror(line, ln, "Rider " + str(rider) + " does not want to go to floor" + str(floor))
	if floor!=elevators[elevator][1]:
		logerror(line, ln, "Elevator " + str(elevator) + " is not at floor " + str(floor))
	return

def rider_check_door(line, ln, elevator):
	if elevators[elevator][0]=='close':
		logerror(line, ln, "Rider tries to get in or out while elevator door is closed!")
	return

def check_button(line, ln, rider, button):
	if riders[rider][0][1]!=button:
		logerror(line, ln, "Rider " + str(button) + " pushes the wrong button!")
	return

def check_rider(line, ln):
	line_array = line.strip().split(" ")
	rider = int(line_array[0][1:])
	check_rider_exist(line,ln,rider)
	action = line_array[1]
	if action == "pushes":
		if line_array[2][0]=='U' or line_array[2][0]=='D':
			direction = line_array[2][0]
			check_in_building(line,ln,rider)
			rider_check_direction(line,ln,rider,direction)
		elif line_array[2][0]=='E':
			elevator = int(line_array[2][1:].split('B')[0])
			check_in_elevator(line,ln,rider,elevator)
			button = int(line_array[2][1:].split('B')[1])
			check_button(line, ln, rider, button)
	elif action == "enters":
		elevator = int(line_array[2][1:])
		floor = int(line_array[4][1:])
		check_in_building(line, ln, rider)
		rider_check_floor(line, ln, rider, floor, elevator, action)
		rider_check_door(line, ln, elevator)
		if part!='part1':
			check_capacity(line, ln, elevator, action)
		riders[rider][0][2] = elevator
		elevators[elevator][2] += 1
	elif action == "exits":
		elevator = int(line_array[2][1:])
		floor = int(line_array[4][1:])
		check_in_elevator(line,ln,rider,elevator)
		rider_check_door(line, ln, elevator)
		rider_check_floor(line, ln, rider, floor, elevator, action)
		riders[rider].pop(0)
		elevators[elevator][2] -= 1
	else:
		logerror(line, ln, "Action " + str(action) + " is not valid for rider!")
	return

def check_output_elevator_format(line, ln):
	line_array = line.strip().split(" ")
	if len(line_array[0])<2:
		logerror(line, ln, "Output format incorrect!")
	output_check_digit(line, ln, line_array[0][1:])
	if len(line_array)<4:
		logerror(line, ln, "Output format incorrect!")
	if line_array[1]=='on':
		if len(line_array)!=4:
			logerror(line, ln, "Output format incorrect!")
		if len(line_array[2])<2:
			logerror(line, ln, "Output format incorrect!")
		if line_array[2][0]!='F':
			logerror(line, ln, "Output format incorrect!")
		output_check_digit(line, ln, line_array[2][1:])
		if line_array[3]!="opens" and line_array[3]!="closes":
			logerror(line, ln, "Output format incorrect!")
	elif line_array[1]=='moves':
		if len(line_array)!=5:
			logerror(line, ln, "Output format incorrect!")
		if line_array[2]!="up" and line_array[2]!="down":
			logerror(line, ln, "Output format incorrect!")
		if line_array[3]!="to":
			logerror(line, ln, "Output format incorrect!")
		if len(line_array[4])<2:
			logerror(line, ln, "Output format incorrect!")
		if line_array[4][0]!='F':
			logerror(line, ln, "Output format incorrect!")
		output_check_digit(line, ln, line_array[4][1:])
	else:
		logerror(line, ln, "Output format incorrect!")
	return

def ele_check_floor(line, ln, elevator, floor):
	onFloor = elevators[elevator][1]
	if onFloor!=floor:
		logerror(line, ln, "Elevator was at floor " + str(onFloor) + ", but try to take action on floor " + str(floor))
	return

def ele_check_door(line, ln, elevator, action):
	if (action=='closes'):
		if elevators[elevator][0]=='close':
			logerror(line, ln, "Elevator was close. Cannot close again!")
	elif (action=='opens'):
		if elevators[elevator][0]=='open':
			logerror(line, ln, "Elevator was open. Cannot open again!")
	return

def check_rider_in_out(line, ln, elevator, floor):
	for rider in riders:
		if len(riders[rider])==0:
			continue
		startfloor = riders[rider][0][0]
		destfloor = riders[rider][0][1]
		inElevator = riders[rider][0][2]
		if inElevator==elevator and destfloor==floor:
			return
		if startfloor==floor:
			return
	logerror(line, ln, "No rider waits and tries to get out at current floor, but elevator " + str(elevator) + " opens door!")
	return

def ele_check_direction(line, ln, elevator, direction, floor):
	onFloor = elevators[elevator][1]
	if (onFloor > floor and direction == "U") or (onFloor < floor and direction == "D"):
		logerror(line, ln, "Elevator " + str(elevator) + " move in wrong direction. Current floor: " + str(onFloor) + " Move to floor: " + str(floor))
	elif onFloor == floor:
		logerror(line, ln, "Elevator " + str(elevator) + " already at floor " + str(floor))
	
def ele_check_floor_connected(line, ln, elevator, floor):
	onFloor = elevators[elevator][1]
	if abs(onFloor - floor) != 1:	
		logerror(line, ln, "Elevator " + str(elevator) + " jumps from floor " + str(onFloor) + " to floor " + str(floor))
	return

def check_elevator(line, ln):
	line_array = line.strip().split(" ")
	elevator = int(line_array[0][1:])
	if elevator not in elevators.keys():
		elevators[elevator] = ['close',1,0]
	if (line_array[1]=='on'):
		floor = int(line_array[2][1:])
		action = line_array[3] 
		ele_check_door(line, ln, elevator, action)
		ele_check_floor(line, ln, elevator, floor)
		if action == 'opens':
			check_rider_in_out(line, ln, elevator, floor)
			elevators[elevator][0] = 'open'
		else:
			elevators[elevator][0] = 'close'
	elif (line_array[1]=='moves'):
		direction = line_array[3]
		floor = int(line_array[4][1:])
		ele_check_direction(line, ln, elevator, direction, floor)
		ele_check_floor_connected(line, ln, elevator, floor)
		elevators[elevator][1] = floor
	return

def check_rider_requests():
	for rider in riders.keys():
		if len(riders[rider])!=0:
			for request in riders[rider]:
				print "Error! Rider " + str(rider) + "'s request from floor " + str(request[0]) + " to floor " + str(request[1]) + " has not been served!" 
				sys.exit()

def check_ele_usage():
	if len(elevators.keys())<num_ele:
		print "Warning: not all available elevators was used to serve requests, " + str(num_ele) + " available but only " + str(len(elevators.keys())) + " was used!"
		return

def parse_output():
	ln = 0
	line = output_file.readline().strip().replace("\n","")
	ln += 1
	while line:
		while line=="":
			line = output_file.readline().strip().replace("\n","")
			ln += 1
			continue
		check_output_format(line,ln)
		firstChar = line.strip().split(" ")[0][0]
		if firstChar=='R':
			check_rider(line, ln)
		elif firstChar == 'E':
			check_elevator(line, ln)
		line = output_file.readline().strip().replace("\n","")
		ln += 1
	check_rider_requests()
	if part!='part1' and part!='part2':
		check_ele_usage()
	print "Output file valid!"
	return

#main

if len (sys.argv) != 4:
	print "Please give input file name, output file name and part of the assignment. e.g. \"input.txt output.txt part3\" Place the log files in the same directory with this script."
	sys.exit()
input_path = sys.argv[1]
output_path = sys.argv[2]
part = sys.argv[3]

try:
	input_file = open(input_path,"r")
except IOError:
	print "Cannot open input file: " + input_file
	sys.exit()

try:
	output_file = open(output_path,"r")
except IOError:
	print "Cannot open output file: " + output_file
	sys.exit()

num_floor = 0
num_ele = 0
num_rider = 0
capacity = 0

riders = {}

inFile = "Input"

parse_input()

input_file.close()

elevators = {}

inFile = "Output"

parse_output()
