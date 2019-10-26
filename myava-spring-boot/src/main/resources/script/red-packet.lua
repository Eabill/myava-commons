---
--- Generated by EmmyLua(https://github.com/EmmyLua)
--- Created by admin.
--- DateTime: 2019/10/26 20:01
---

local key = KEYS[1]..ARGV[1]
local listKey = key..':list'
local recKey = key..':received'
if redis.call('sadd', recKey, ARGV[2]) == 0 then
    return 'RECEIVED'
else
    local _redPacket = redis.call('rpop', listKey)
    if _redPacket then
        local redPacket = cjson.decode(_redPacket)
        redPacket['userId'] = ARGV[2]
        local _remain = redis.call('get', key)
        if _remain then
            local remain = cjson.decode(_remain);
            remain['remainNum'] = tonumber(remain['remainNum']) - 1
            remain['remainAmt'] = tonumber(remain['remainAmt']) - tonumber(redPacket['amount'])
            redis.call('set', key, cjson.encode(remain))
        end
        return cjson.encode(redPacket)
    end
end
return 'NOT_HAVE_RED_PACKET'
