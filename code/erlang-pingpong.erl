#!/usr/bin/env escript

pong(Name, Main) ->
    receive
        {ping, Count, Ping_PID} ->
            io:format("~s received ping, count down ~w~n", [Name, Count]),
            if Count > 0 ->
              Ping_PID ! {ping, Count - 1, self()},
              pong(Name, Main);
            true ->
              exit ("done")
            end
    end.

main(_) ->
    Pong1_PID = spawn(fun() -> pong("pinger", self()) end),
    Pong2_PID = spawn(fun() -> pong("ponger", self()) end),
    Pong1_PID ! {ping, 10, Pong2_PID},
    timer:sleep(100),
    ok.
