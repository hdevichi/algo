# -*- coding: utf-8 -*-
class Sudoku:

    width = 9
    height = 9
    # -1 means empty
    board = [ [-1 for x in range(width)] for y in range(height)]
    empty = width * height

    def __init__(self, board):
        self.height = len(board)
        self.width = len(board[0])
        self.board = board
        
        self.empty = 0
        for y in range(len(board)):
            for x in range(len(board[y])):
                if board[y][x] == -1:
                    self.empty = self.empty +1
        
    def clearPosition(self, x, y):
        self.board[y][x] = -1
        self.empty = self.empty + 1

    def setPosition(self, x, y, value):
        self.board[y][x] = value
        self.empty = self.empty - 1

    def getPosition(self, x, y):
        return self.board[y][x]

    def isValueValid(self, x, y, value):
        
        # check line
        for i in range(self.width):
            if i == x:
                continue
            if value == self.getPosition(i,y):
                return False

        # check column
        for j in range(self.height):
            if j == y:
                continue
            if value == self.getPosition(x,j):
                return False

        # check sub grid (0-3) (3-6) (6-9)
        sectionX = 3*(x//3)
        sectionY = 3*(y//3)
        for i in range(sectionX, sectionX+3):
            for j in range(sectionY, sectionY+3):
                if (x == i) and (y == j):
                    continue
                if value == self.getPosition(i,j):
                    return False
        
        return True

    # test validity, but not completion
    def isValid(self):

        for x in range(self.width):
            for y in range(self.height):
                value = self.getPosition(x, y)
                if value == -1:
                    continue
                if self.isValueValid(x,y, value) == False:
                    return False

        return True

    def computeValidValuesForPosition(self, x, y):

        value = self.getPosition(x, y)
        if value != -1:
            return []
            
        valid = []
        for candidate in range(1, 10):
            if self.isValueValid(x, y, candidate):
                valid.append(candidate)

        return valid        
    
    def solve(self):

        if self.isValid() == False:
            print('Invalid start solution for solve!')
            return

        try:
            self.recursiveSolve()
            print('No solution found.')
        except:
            print('Solution found !')
            print(self.board)
            pass

        

    def recursiveSolve(self):

        if self.isValid() == False:
            return

        if self.empty == 0:
            raise Exception('Found')
        
        # find next most constrained position (candidate)
        candidatePosition = (0,0)
        numberOfValidValues = 9
        valid = []
        for x in range(self.width):
            for y in range(self.height):
                validValues = self.computeValidValuesForPosition(x,y)
                if len(validValues) == 0:
                    continue
                if len(validValues) < numberOfValidValues:
                    candidatePosition = (x,y)
                    numberOfValidValues = len(validValues)
                    valid = validValues
        
        for value in valid:
            self.setPosition(candidatePosition[0], candidatePosition[1], value)
            self.recursiveSolve()
            self.clearPosition(candidatePosition[0], candidatePosition[1])


if __name__ == '__main__':
    
    grille = Sudoku([   [-1, -1, -1, -1, -1, -1, -1, -1, -1 ],
                        [9, 1, 4, 5, 7, -1, -1, 2, 6 ],
                        [5, 2, 8, 6, -1, 1, 4, 9, -1 ],
                        [-1, -1, -1, -1, -1, 3, 2, -1, 4 ],
                        [-1, 6, 3, 1, -1, 9, 5, 7, -1 ],
                        [8, -1, 1, 2, -1, -1, -1, -1, 3 ],
                        [-1, 5, 2, 7, -1, 4, -1, -1, 9 ],
                        [6, 3, -1, 9, -1, -1, 8, 4, -1 ],
                        [4, -1, -1, 3, 2, -1, -1, 5, -1 ]
                     ])

    import datetime
    start = datetime.datetime.now()
    grille.solve()
    end = datetime.datetime.now()
    duration = (end-start).total_seconds() * 1000 * 1000
    print('Solved in '+str(duration)+ ' µs')
    
