#! /usr/bin/env elixir

defmodule Pingponger do
  def apply(name) do
    receive do
      {:ping, count, sender} ->
        IO.puts "#{name} received ping, count down #{count}"
        if count > 0 do
          send sender, {:ping, count - 1, self()}
          apply(name)
        else
          :ok
        end
    end
  end
end

pid1 = spawn fn -> Pingponger.apply("pinger") end
pid2 = spawn fn -> Pingponger.apply("ponger") end

send pid1, {:ping, 10, pid2}

defmodule Main do
  def wait(pid) do
    wait(pid, true)
  end

  def wait(pid, alive) do
    case alive do
      false ->
        :ok
      true ->
        wait(pid, Process.alive?(pid))
    end
  end
end

Main.wait(pid1)
