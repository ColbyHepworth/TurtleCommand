

local directions = { "north", "east", "south", "west" }

local directionChanges = {
    north = {east = "right", south = "around", west = "left"},
    east = {south = "right", west = "around", north = "left"},
    south = {west = "right", north = "around", east = "left"},
    west = {north = "right", east = "around", south = "left"},
}

local bumps = {
    north = { 0,  0, -1 },
    south = { 0,  0,  1 },
    east  = { 1,  0,  0 },
    west  = {-1,  0,  0 },
    up = {0, 1, 0},
    down = {0, -1, 0},
}

local left_shift = {
    north = 'west',
    south = 'east',
    east  = 'north',
    west  = 'south',
}


local right_shift = {
    north = 'east',
    south = 'west',
    east  = 'south',
    west  = 'north',
}


local reverse_shift = {
    north = 'south',
    south = 'north',
    east  = 'west',
    west  = 'east',
}

local reverseDirection =  {
    north = "south",
    south = "north",
    east = "west",
    west = "east"
}

local directionInputs = {
    north = 0,
    south = 0,
    east = 0,
    west = 0,
    up = 0,
    down = 0
}


local id = os.getComputerID()
local socket = http.websocket("wss://72f2-2600-1700-3d70-c7a9-f817-332b-cc31-9d06.ngrok.io/turtles")


local currentDirection = 1



local function getCurrentDirection()
    return directions[currentDirection]
end

local function getCurrentPosition()
    return gps.locate()
end

local function increasePostion(direction)
    direction = direction or getCurrentDirection()
    local x, y, z = getCurrentPosition()
    local direction = getCurrentDirection()
    local bump = bumps[direction]
    return x + bump[1], y + bump[2], z + bump[3]
end

local scannedData = {}
local function update()
    
    local x, y, z = getCurrentPosition()
    
    
    local data = {
        id = id,
        position = {
          x = x,
          y = y,
          z = z,
        },
        direction = getCurrentDirection(),
        fuel = turtle.getFuelLevel(),
        status = {isMoving = true, isDigging = false},
        scanned = scannedData
      }
    
      socket.send(textutils.serializeJSON(data))
      
end

local function serializePosition(x, y, z)
    return  {
            x = x,
            y = y,
            z = z
            }
    
end

local function getScan(direction)
    direction = direction or "forward"
    
    
    if direction == "up" then
        local hasBlock, blockData = turtle.inspect()
        if hasBlock then
            return hasBlock, blockData.name
        else
            return hasBlock, "empty"
        end
    end

    if direction == "down" then
        local hasBlock, blockData = turtle.inspectDown()
        if hasBlock then
            return hasBlock, blockData.name   
        else
            return hasBlock, "empty"
        end
    end

    if direction == "forward" then
        local hasBlock, blockData = turtle.inspect()
        if hasBlock then
            return hasBlock, blockData.name
        else
            return hasBlock, "empty"
        end
    end
end


local function scanBlock(direction)
    direction = direction or "forward"
    local hasBlock, blockName = getScan(direction)

    local x, y, z = increasePostion(direction)
    local position = serializePosition(x, y, z)
    
    local scannedBlock = {
        position = position,
        name = {blockName}
    }
    table.insert(scannedData, scannedBlock)

    
end

local function scan(scanVertical)
    scanVertical = scanVertical or true
    scanBlock("forward")
    if scanVertical then
        scanBlock("up")
        scanBlock("down")
    end
end


local function scanAll() 
    local x, y, z = getCurrentPosition()
    local hasBlock, blockData = turtle.inspect()
    local blockName = blockData.name
    if hasBlock then
        scannedData[blockName] = increasePostion("forward")
    end
    
end


local function turnRight()
    currentDirection = (currentDirection % #directions) + 1
    turtle.turnRight()
    scan()
end

local function turnLeft()
    currentDirection = ((currentDirection - 2 + #directions) % #directions) + 1
    turtle.turnLeft()
    scan()
end


local function turnAround(direction)
    direction = direction or "left"
    if direction == "left" then
        turnLeft()
        turnLeft()
    end
    if direction == "right" then
        turnRight()
        turnRight()
    end
    update()
end

local function turnTo(direction) 
    local currentDirection = getCurrentDirection()
    if currentDirection == direction then
        return
    end
    local change = directionChanges[currentDirection][direction]
    if change == "right" then
        turnRight()
    elseif change == "left" then
        turnLeft()
    elseif change == "around" then
        turnAround()
    end
    update()
end

local scanMode = false;

local function forward()
    scan()
    return turtle.forward()
end

local function up()
    scan()
    return turtle.up()
end

local function down()
    scan()
    return turtle.down()
end

local function north()
    turnTo("north")
    return forward()
end

local function south()
    turnTo("south")
    return forward()
end

local function east()
    turnTo("east")
    return forward()
end

local function west()
    turnTo("west")
    return forward()
end

local function dig(direction)
    if direction == "up" then
        turtle.digUp()
    elseif direction == "down" then
        turtle.digDown()
    else
        turnTo(direction)
        turtle.dig()
    end
end

local function move(direction, distance, safe)
    safe = safe or false
    distance = distance or 1

    if direction == "up" then
        for i = 1, distance do
            update()
            if not up() then
                if safe then
                    return false
                else
                    dig("up")
                    up()
                end
            end
        end
    elseif direction == "down" then
        for i = 1, distance do
            update()
            if not down() then
                if safe then
                    return false
                else
                    dig("down")
                    down()
                end
            end
        end
    else
        turnTo(direction)
        for i = 1, distance do
            update()
            if not forward() then
                if safe then
                    return false
                else
                    dig(direction)
                    forward()
                end
            end
        end
    end
end


local function moveTo(x, y, z)
    local startX, startY, startZ = getCurrentPosition()
    local dx = math.abs(math.abs(x) - math.abs(startX))
    local dy = math.abs(math.abs(y) - math.abs(startY))
    local dz = math.abs(math.abs(z) - math.abs(startZ))
    if y > startY then
        move("up", dy)
    elseif y < startY then
        move("down", dy)
    end

    if x > startX then
        move("east", dx)
    elseif x < startX then
        move("west", dx)
    end

    if z > startZ then
        move("south", dz)
    elseif z < startZ then
        move("north", dz)
    end
    scan()
    update()
end

local function calibrate()
    print("Calibrating")
    
    local startX, startY, startZ = gps.locate()
    for i = 1, 4 do
        if not turtle.detect() then
            break
        else
            turtle.turnLeft()
        end
    end
    forward()
    local newX, newY, newZ = gps.locate()
    if newX == startX + 1 then
        currentDirection = 2
    elseif newX == startX - 1 then
        currentDirection = 4
    elseif newZ == startZ + 1 then
        currentDirection = 3
    elseif newZ == startZ - 1 then
        currentDirection = 1
    else
        return false
    end
    local calibratedDirection = getCurrentDirection()
    turnAround()
    forward()
    turnTo(calibratedDirection)
    update()
end

local path = {
    {-10, -59, 15},
{-11, -59, 15},
{-11, -58, 15},
{-11, -58, 14},
{-11, -58, 13},
{-11, -58, 12},
{-11, -58, 11},
{-11, -58, 10},
}

calibrate()


local function movePath()
    for i = 1, #path do
        moveTo(path[i][1], path[i][2], path[i][3])
    end
end

movePath()

