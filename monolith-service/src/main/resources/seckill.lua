
-- 查找库存
local stock = tonumber(redis.call('get',KEYS[1]))

if stock == nil or stock <= 0 then
    return -1
end

-- 判断秒杀
local isMember = redis.call('sismember', KEYS[2], ARGV[1])

if isMember == 1 then
    return -2
end

-- 扣库存
redis.call('decrby', KEYS[1], 1)



-- 记录用户已秒杀
redis.call('sadd', KEYS[2], ARGV[1])

return 0